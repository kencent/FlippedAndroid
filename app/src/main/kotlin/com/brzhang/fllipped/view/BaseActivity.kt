package com.brzhang.fllipped.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brzhang.fllipped.RxBus
import com.brzhang.fllipped.busevent.UserAuthFailed
import rx.subscriptions.CompositeSubscription

/**
 *
 * Created by brzhang on 2017/7/6.
 * Description :
 */
abstract class BaseActivity : AppCompatActivity() {
    val mSubScription = CompositeSubscription()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBusEvent()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSubScription.clear()
    }

    private fun setupBusEvent() {

        RxBus.getRxBusSingleton().subscribe(mSubScription, {
            event: Any? ->
            when (event) {
                is UserAuthFailed -> {
                    startLoginActivity()
                }
            }
        })
    }

    private fun startLoginActivity() {
        if (this is LoginActivity) {
            return
        }
        startActivity(Intent(this, LoginActivity::class.java))
    }
}