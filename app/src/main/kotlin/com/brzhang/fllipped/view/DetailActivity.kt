package com.brzhang.fllipped.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.brzhang.fllipped.RxBus
import com.brzhang.fllipped.busevent.DeleteFlipped
import com.brzhang.fllipped.model.Comment
import com.brzhang.fllipped.model.CommentListResponse
import com.brzhang.fllipped.model.Content
import retrofit2.Response


/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */
class DetailActivity : FlippedBaseActivity(), OnPreparedListener {

    private var mFlippedId = ""
    private var mFlipped: Flippedword? = null

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

    private var mCommentNum: TextView? = null

    override fun handleRxEvent(event: Any?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_flipped_detail
    }

    override fun onOptionHomeClick() {
        this.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (!FlippedHelper.canDelete(mFlipped)) {
            menu?.findItem(R.id.op_delete)?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.op_delete -> {
                if (FlippedHelper.canDelete(mFlipped)) {
                    MaterialDialog.Builder(this)
                            .title("提示")
                            .content("确认删除该条心动的话？")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive { dialog, which ->
                                deleteFlippedWords(mFlipped)
                            }
                            .show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteFlippedWords(fliipped: Flippedword?) {
        showProgressBar()
        fllippedNetService().deleteFllipped(fliipped?.id ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Void>>() {
                    override fun onCompleted() {
                        hideProgressBar()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("hoolly", "error", e)
                        hideProgressBar()
                    }

                    override fun onNext(responseBody: Response<Void>) {
                        toast("删除成功")
                        RxBus.getRxBusSingleton().send(DeleteFlipped(fliipped!!))
                        finish()
                    }
                })
    }

    override fun setupView(view: View) {
        msendTo = flipped_detail_tv_send_to
        mtext = flipped_detail_text
        mAudio = flipped_detail_audio
        mImage = flipped_detail_image
        mVideo = flipped_detail_video
        mLlComment = flipped_detail_comment_ll
        mRecyclerView = flipped_detail_comment_recycler_view
        mCommentNum = flipped_detail_iv_comment_all
        mLlComment?.setOnClickListener({
            showCommentDialog()
        })
        mVideo?.setOnPreparedListener(this)
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        adapter = CommentAdapter()
        mRecyclerView?.adapter = adapter
        showNavigationBack()
        /*显示底部评论按钮*/
        showFloatActionButton(View.OnClickListener {
            showCommentDialog()
        })
        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var layoumaner = recyclerView?.layoutManager as LinearLayoutManager
                if (layoumaner.findLastVisibleItemPosition() == recyclerView?.adapter.itemCount - 1) {
                    loadMoreComment()
                }
            }
        })
    }

    private fun loadMoreComment() {

        dToast("加载更多评论数据")
    }

    private fun showCommentDialog() {
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
        mFlipped = t
        supportInvalidateOptionsMenu()
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
        if (mFlipped?.commentnum != null && mFlipped?.commentnum != 0) {
            mCommentNum?.text = """${mCommentNum?.text.toString()} ${mFlipped?.commentnum.toString()}"""
        }
    }

    inner class CommentAdapter : RecyclerView.Adapter<CommentViewHolder>() {
        var comments: ArrayList<Comment> = arrayListOf()

        fun addComment(comment: Comment) {
            comments.add(comment)
            notifyItemInserted(itemCount)
        }

        fun deleteComment(comment: Comment) {
            var i = 0
            for (com in comments) {
                if (com.id!!.equals(comment.id)) {
                    comments.removeAt(i)
                    notifyItemRemoved(i)
                    break
                } else {
                    i++
                }
            }
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: CommentViewHolder?, position: Int) {
            holder?.send?.text = """${comments[position].uid}说："""
            holder?.floor?.text = """${comments[position].floor}楼"""
            holder?.contentText?.text = comments[position].contents?.get(0)?.text
            val comment = comments[position]
            holder?.itemView?.setOnLongClickListener {
                if (canDeleteComment(comment)) {
                    showDeleteComment(comment)
                }
                true
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentViewHolder {
            return CommentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.flipped_comment_item, parent, false))
        }

        override fun getItemCount(): Int {
            return comments.size
        }

    }

    private fun showDeleteComment(comment: Comment) {
        MaterialDialog.Builder(this)
                .title("提示")
                .content("确认删除该评论？")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive { dialog, which ->
                    deleteComment(comment)
                }
                .show()
    }

    private fun deleteComment(comment: Comment) {
        showProgressBar()
        fllippedNetService().deleteComment(mFlippedId.toInt(), comment.id ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Void>>() {
                    override fun onCompleted() {
                        hideProgressBar()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("hoolly", "error", e)
                        hideProgressBar()
                    }

                    override fun onNext(responseBody: Response<Void>) {
                        //todo 这里处理删除逻辑，移除改条
                        toast("删除评论成功")
                        adapter?.deleteComment(comment)
                    }
                })
    }

    private fun canDeleteComment(comment: Comment): Boolean {
        if (comment?.links == null || comment?.links!!.isEmpty()) {
            return false
        }
        return comment.links!!.any { it.method.toUpperCase().equals("DELETE") }
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