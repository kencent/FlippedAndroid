package com.brzhang.fllipped.view

import android.content.Context
import android.content.Intent
import android.view.View
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.Content
import com.brzhang.fllipped.model.Flippedword
import kotlinx.android.synthetic.main.activity_about.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */

class AboutActivity : FlippedBaseActivity() {
    override fun handleRxEvent(event: Any?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_about
    }

    override fun setupView(view: View) {
        initData()
    }

    private fun initData() {

        fllippedNetService().getHelp().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Flippedword>() {
                    override fun onNext(t: Flippedword?) {
                        showHelp(t?.contents)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        toast("获取帮助文件失败")
                    }
                })
    }

    private fun showHelp(contents: List<Content>?) {
        contents?.forEach { content ->
            activity_about_text.text = "${activity_about_text.text} \"\n\" ${content.text}"
        }
    }

    companion object {
        fun startMe(context: Context){
            var intent = Intent(context,AboutActivity::class.java)
            context.startActivity(intent)
        }
    }
}