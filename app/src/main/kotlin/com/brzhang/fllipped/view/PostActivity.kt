package com.brzhang.fllipped.view

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.brzhang.fllipped.R
import com.brzhang.fllipped.SignResponse
import com.brzhang.fllipped.model.Content
import com.brzhang.fllipped.model.Flippedword
import com.brzhang.fllipped.utils.LogUtil
import com.brzhang.fllipped.utils.UploadUtils
import com.bumptech.glide.Glide
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_post.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */
class PostActivity : FlippedBaseActivity() {

    val REQUEST_CODE_CHOOSE = 1002

    var mSelected = ArrayList<Uri>()
    private var mPhone: EditText? = null

    private var mText: EditText? = null

    private var mImageRl: RelativeLayout? = null

    private var mImageContent: ImageView? = null

    private var mVideoRl: RelativeLayout? = null

    private var mAudioRl: RelativeLayout? = null

    var subscriber = object : Subscriber<Flippedword>() {
        override fun onNext(t: Flippedword?) {
            LogUtil.dLoge("hoolly",t?.id.toString())
        }

        override fun onCompleted() {

        }

        override fun onError(e: Throwable?) {
            LogUtil.dLoge("hoolly",e?.message)
        }
    }

    override fun handleRxEvent(event: Any?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_post
    }

    override fun setupView(view: View) {
        showNavigationBack()
        setActTitle(resources.getString(R.string.activity_post_title))
        showRigthText("发布", View.OnClickListener {
            postFlipped()
        })
        mPhone = flipped_post_activity_et_phone
        mText = flipped_post_activity_et_words
        mImageRl = activity_post_add_image_rl
        mImageContent = activity_post_add_image_iv_content
        mVideoRl = activity_post_add_video_rl
        mAudioRl = activity_post_add_audio_rl

        mImageRl?.setOnClickListener({
            selectImage()
        })
        mVideoRl?.setOnClickListener({
            selectVideo()
        })
        mAudioRl?.setOnClickListener({
            selectAudio()
        })
    }

    private fun selectAudio() {

    }

    private fun selectVideo() {


    }

    private fun selectImage() {
        if (mImageContent?.visibility == View.GONE) {
            rxPermissions?.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ?.subscribe { grant ->
                        if (grant) {
                            Matisse.from(this)
                                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                    .theme(R.style.Matisse_Dracula)
                                    .countable(true)
                                    .maxSelectable(1)
//                    .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE)
                        } else {
                            toast("请到权限中开启授权")
                        }
                    }

        } else {
            showDeleteSelectedImage()
        }
    }

    private fun showDeleteSelectedImage() {
        MaterialDialog.Builder(this)
                .title("确认删除图片")
                .positiveText("确认")
                .neutralText("取消")
                .onPositive { dialog, which ->
                    // TODO
                    mImageContent?.visibility = View.GONE
                    mSelected.clear()
                }
                .onNeutral { dialog, which ->
                    // TODO
                }
                .onNegative { dialog, which ->
                    // TODO
                }
                .onAny { dialog, which ->
                    // TODO
                }
                .show()
    }


    private fun postFlipped() {


        if (mSelected?.size > 0) {
            val path = UploadUtils.UriToPath(this, mSelected[0])
            fllippedNetService()
                    .getSign()
                    .subscribeOn(Schedulers.io())
                    .flatMap { t: SignResponse? ->
                        UploadUtils.uploadImage(t!!.sig, path)
                    }
                    .flatMap { t: String? ->
                        var flippedWord = Flippedword()
                        flippedWord.sendto = mPhone?.text.toString()
                        val contentText = Content()
                        contentText.type = Content.Type.TEXT.name
                        contentText.text = mText.toString()
                        val contentImage = Content()
                        contentImage.type = Content.Type.PICUTRE.name
                        contentImage.text = t
                        flippedWord.contents = mutableListOf(contentText, contentImage)
                        fllippedNetService().createFllipped(flippedWord)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber)
        } else {
            var flippedWord = Flippedword()
            flippedWord.sendto = mPhone?.text.toString()
            val contentText = Content()
            contentText.type = Content.Type.TEXT.name
            contentText.text = mText.toString()
            flippedWord.contents = mutableListOf(contentText)
            fllippedNetService().createFllipped(flippedWord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data) as ArrayList<Uri>
            LogUtil.dLoge("Matisse", "mSelected: " + mSelected)
            mImageContent?.visibility = View.VISIBLE
            Glide.with(this).load(mSelected[0]).into(mImageContent)
        }
    }

}