package com.brzhang.fllipped.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.brzhang.fllipped.FlippedHelper
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.Flippedword
import com.bumptech.glide.Glide
import com.devbrackets.android.exomedia.listener.OnPreparedListener
import com.devbrackets.android.exomedia.ui.widget.VideoView
import kotlinx.android.synthetic.main.activity_flipped_detail.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import com.afollestad.materialdialogs.MaterialDialog
import android.R.id.input
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brzhang.fllipped.model.Comment
import com.brzhang.fllipped.model.CommentListResponse
import com.brzhang.fllipped.model.Content
import com.brzhang.fllipped.pref.UserPref


/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */
class DetailActivity : FlippedBaseActivity(), OnPreparedListener {

    private var mFlippedId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFlippedId = intent.getStringExtra(FlippedId)
        if (mFlippedId.isEmpty()) {
            finish()
        }
        initData()
    }

    private var msendTo: TextView? = null

    private var mtext: TextView? = null

    private var mAudio: VideoView? = null

    private var mImage: ImageView? = null

    private var mVideo: VideoView? = null

    private var mLlComment: RelativeLayout? = null

    private var mRecyclerView: RecyclerView? = null

    private var adapter: CommentAdapter? = null

    override fun handleRxEvent(event: Any?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_flipped_detail
    }

    override fun onOptionHomeClick() {
        this.finish()
    }


    override fun setupView(view: View) {
        msendTo = flipped_detail_tv_send_to
        mtext = flipped_detail_text
        mAudio = flipped_detail_audio
        mImage = flipped_detail_image
        mVideo = flipped_detail_video
        mLlComment = flipped_detail_comment_ll
        mRecyclerView = flipped_detail_comment_recycler_view
        mLlComment?.setOnClickListener({
            MaterialDialog.Builder(this)
                    .title("请输入评论内容")
                    .inputRangeRes(1, 90, R.color.colorPrimaryDark)
                    .input(null, null, MaterialDialog.InputCallback { dialog, input ->
                        // Do something
                        var comment = Comment()
                        var content = Content()
                        content.text = input.toString()
                        content.type = Content.Type.TEXT.type
                        comment.contents = arrayListOf(content)
                        postComment(comment)
                    }).show()
        })
        mVideo?.setOnPreparedListener(this)
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        adapter = CommentAdapter()
        mRecyclerView?.adapter = adapter
        showNavigationBack()
    }

    private fun postComment(comment: Comment) {
        showProgressBar()
        fllippedNetService().comment(mFlippedId, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Comment>() {
                    override fun onNext(t: Comment?) {
                        toast("评论成功")
                        comment.id = t?.id
                        comment.floor = t?.floor
                        comment.uid = /*UserPref.getUserName(applicationContext, "无名小辈")*/ "我"
                        adapter?.addComment(comment)
                    }

                    override fun onCompleted() {
                        hideProgressBar()
                    }

                    override fun onError(e: Throwable?) {
                        toast(e?.message ?: "评论失败")
                        hideProgressBar()
                    }
                })
    }

    override fun onPrepared() {
        mVideo?.start()
    }

    private fun initData() {

        showProgressBar()
        fllippedNetService().getFlippedDetail(mFlippedId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Flippedword>() {
                    override fun onNext(t: Flippedword?) {
                        showFlippedDetail(t)
                    }

                    override fun onCompleted() {

                        hideProgressBar()
                    }

                    override fun onError(e: Throwable?) {
                        toast(e?.message ?: "查询详情失败")
                        hideProgressBar()
                    }
                })
        fllippedNetService().listComments(mFlippedId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<CommentListResponse>() {
                    override fun onNext(t: CommentListResponse?) {
                        adapter?.comments = t?.comments ?: arrayListOf()
                        adapter?.notifyDataSetChanged()
                    }

                    override fun onCompleted() {
                        hideProgressBar()
                    }

                    override fun onError(e: Throwable?) {
                        toast(e?.message ?: "查询评论失败")
                        hideProgressBar()
                    }
                })

    }

    private fun showFlippedDetail(t: Flippedword?) {
        msendTo?.text = "发送给：${t?.sendto}"
        if (FlippedHelper.getText(t).isBlank()) {
            mtext?.visibility = View.GONE
        } else {
            mtext?.visibility = View.VISIBLE
            mtext?.text = FlippedHelper.getText(t)
        }
        if (FlippedHelper.getVoice(t).isBlank()) {
            mAudio?.visibility = View.GONE
        } else {
            mAudio?.visibility = View.VISIBLE
//            mAudio?.text = FlippedHelper.getText(t)
            mAudio?.setVideoURI(Uri.parse(FlippedHelper.getVoice(t)))
        }
        if (FlippedHelper.getPic(t).isBlank()) {
            mImage?.visibility = View.GONE
        } else {
            mImage?.visibility = View.VISIBLE
            Glide.with(this).load(FlippedHelper.getPic(t)).into(mImage)
        }
        if (FlippedHelper.getVideo(t).isBlank()) {
            mVideo?.visibility = View.GONE
        } else {
            mVideo?.visibility = View.VISIBLE
            mVideo?.setVideoURI(Uri.parse(FlippedHelper.getVideo(t)))
        }
    }

    inner class CommentAdapter : RecyclerView.Adapter<CommentViewHolder>() {
        var comments: ArrayList<Comment> = arrayListOf()

        fun addComment(comment: Comment) {
            comments.add(comment)
            notifyItemInserted(itemCount)
        }

        override fun onBindViewHolder(holder: CommentViewHolder?, position: Int) {
            holder?.send?.text = """${comments[position].uid}说："""
            holder?.floor?.text = """${comments[position].floor}楼"""
            holder?.contentText?.text = comments[position].contents?.get(0)?.text
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentViewHolder {
            return CommentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.flipped_comment_item, parent, false))
        }

        override fun getItemCount(): Int {
            return comments.size
        }

    }

    inner class CommentViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var send: TextView = itemView?.findViewById(R.id.flipped_comment_item_tv_sender) as TextView
        var floor: TextView = itemView?.findViewById(R.id.flipped_comment_item_tv_floor) as TextView
        var contentText: TextView = itemView?.findViewById(R.id.flipped_comment_item_tv_content_text) as TextView
    }

    companion object {
        val FlippedId: String = "FlippedId"
        fun startMe(context: Context, flippedId: String) {
            val intent: Intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(FlippedId, flippedId)
            context.startActivity(intent)
        }
    }


}