package com.brzhang.fllipped.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.brzhang.fllipped.BuildConfig
import com.brzhang.fllipped.RxBus
import com.brzhang.fllipped.busevent.UserAuthFailed
import com.brzhang.fllipped.pref.UserPref
import com.tbruyelle.rxpermissions.RxPermissions
import rx.subscriptions.CompositeSubscription

/**
 *
 * Created by brzhang on 2017/7/6.
 * Description :
 */
abstract class BaseActivity : AppCompatActivity() {
    val mSubScription = CompositeSubscription()
    var rxPermissions: RxPermissions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBusEvent()
        rxPermissions = RxPermissions(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSubScription.clear()
    }

    fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
    }

    fun dToast(string: String) {
        if (BuildConfig.DEBUG) {
            toast(string)
        }
    }

    private fun setupBusEvent() {

        RxBus.getRxBusSingleton().subscribe(mSubScription, {
            event: Any? ->
            when (event) {
                is UserAuthFailed -> {
                    UserPref.setUserLogin(this, false)
                    gotoLoginActivity()
                }
                else -> {//交给子类处理其他事件
                    handleRxEvent(event)
                }
            }
        })
    }

    abstract fun handleRxEvent(event: Any?)

    protected fun gotoLoginActivity() {
        if (this is LoginActivity) {
            return
        }
        startActivity(Intent(this, LoginActivity::class.java))
    }
}