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
import com.brzhang.fllipped.model.Comment
import com.brzhang.fllipped.model.Content


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

    companion object {
        val FlippedId: String = "FlippedId"
        fun startMe(context: Context, flippedId: String) {
            val intent: Intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(FlippedId, flippedId)
            context.startActivity(intent)
        }
    }


}