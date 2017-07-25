package com.brzhang.fllipped.view

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.Content
import com.brzhang.fllipped.model.Flippedword
import kotlinx.android.synthetic.main.activity_report.*
import okhttp3.ResponseBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by brzhang on 2017/7/10.
 */
class ReportActivity : FlippedBaseActivity() {

    override fun onOptionHomeClick() {
        super.onOptionHomeClick()
        this.finish()
    }

    override fun handleRxEvent(event: Any?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_report
    }

    private var mReportContent: EditText? = null

    override fun setupView(view: View) {
        mReportContent = activity_report_content_et
        activity_report_btn.setOnClickListener {
            doReport()
        }
        showNavigationBack()
    }

    private fun doReport() {
        if (mReportContent?.text.toString().isBlank()) {
            toast("说点什么吧~~")
        }

        var content  = Content()
        content.text = mReportContent?.text.toString()
        content.type = Content.Type.TEXT.type
        var flipped = Flippedword()
        flipped.contents = listOf(content)
        fllippedNetService().feedbacks(flipped).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onNext(t: ResponseBody?) {
                        toast("反馈成功，感谢~~")
                    }

                    override fun onCompleted() {
                        finish()
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
    }

    companion object {
        fun startReport(context: Context){
            val intent = Intent(context,ReportActivity::class.java)
            context.startActivity(intent)
        }
    }


}