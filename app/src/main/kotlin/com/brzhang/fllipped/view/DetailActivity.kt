package com.brzhang.fllipped.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import com.brzhang.fllipped.FlippedHelper
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.Flippedword
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_flipped_detail.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscriber
import rx.schedulers.Schedulers

/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */
class DetailActivity : FlippedBaseActivity() {

    public val FlippedId = "FlippedId"

    private var mFlippedId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFlippedId = intent.getStringExtra(FlippedId)
        if (mFlippedId.isEmpty()) {
            finish()
        }
    }

    private var msendTo: TextView? = null

    private var mtext: TextView? = null

    private var mAudio: MediaController? = null

    private var mImage: ImageView? = null

    private var mVideo: VideoView? = null


    override fun handleRxEvent(event: Any?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_flipped_detail
    }

    override fun setupView(view: View) {
        msendTo = flipped_detail_tv_send_to
        mtext = flipped_detail_text
        mAudio = flipped_detail_audio
        mImage = flipped_detail_image
        mVideo = flipped_detail_video

        initData()
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
        msendTo?.text = t?.sendto
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
            TODO("play audio")
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
            TODO("play video")
        }
    }


}