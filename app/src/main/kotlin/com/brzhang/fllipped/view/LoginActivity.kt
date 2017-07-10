package com.brzhang.fllipped.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.VeryCodeResponse
import com.brzhang.fllipped.pref.UserPref
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : FlippedBaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_login
    }

    override fun onOptionHomeClick() {
        this.finish()
    }

    override fun setupView(view: View) {
        hideRight()
        hideNavigationBack()
        setActTitle("登录")

        flipped_login_tv_about.setOnClickListener(this)
        flipped_login_bt_get_very_code.setOnClickListener(this)
        flipped_login_bt_login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.flipped_login_bt_login -> {
                doLogin()
            }
            R.id.flipped_login_bt_get_very_code -> {
                doGetVeryCode()
            }
            R.id.flipped_login_tv_about -> {
                startAboutActivity()
            }
        }
    }

    private fun startAboutActivity() {
        AboutActivity.startMe(this)
    }

    private fun doGetVeryCode() {
        if (!isValidPhone()) {
            toast("请输入正确手机号")
            return
        }
        UserPref.setUserName(this, flipped_login_et_phone.text.toString())
        startGetVeryCodeBtCoundDown()
        fllippedNetService()
                .getVeryCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<VeryCodeResponse>() {
                    override fun onNext(t: VeryCodeResponse?) {
                        toast("获取验证成功")
                        storeSalt(t?.s ?: "")
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        toast("获取验证码失败" + e.toString())
                    }
                })
    }

    /*获取验证码的bt等待即使*/
    private fun startGetVeryCodeBtCoundDown() {
        MyCoundDown(button = flipped_login_bt_get_very_code,
                countSecond = 60,
                millisInFuture = 60 * 1000,
                countDownInterval = 1000).start()
    }

    private fun doLogin() {
        if (!isValidPhone()) {
            toast("请输入正确的手机号")
            return
        }
        if (!isValidVeryCode()) {
            toast("请输入验证码")
            return
        }
        UserPref.setUserLogin(this, false)
        UserPref.setUserName(this, flipped_login_et_phone.text.toString())
        UserPref.setUserPassWord(this, flipped_login_et_password.text.toString())
        fllippedNetService().login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onNext(t: ResponseBody?) {
                        toast("登陆成功")
                        setLoginSuccess()
                    }

                    override fun onError(e: Throwable?) {
                        toast("登陆失败" + e.toString())
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun isValidPhone(): Boolean {
        return !flipped_login_et_phone.text.isEmpty()
    }

    private fun isValidVeryCode(): Boolean {
        return !flipped_login_et_password.text.isEmpty()
    }

    private fun storeSalt(salt: String) {
        UserPref.setUserSalt(this, salt)
    }

    private fun setLoginSuccess() {
        UserPref.setUserLogin(this, true)
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun handleRxEvent(event: Any?) {

    }

    internal class MyCoundDown
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * *                          to [.start] until the countdown is done and [.onFinish]
     * *                          is called.
     * *
     * @param countDownInterval The interval along the way to receive
     * *                          [.onTick] callbacks.
     */
    (private val button: Button,
     private var countSecond: Int,
     millisInFuture: Long,
     countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            button.isEnabled = false
            button.text = "${--countSecond}s后重新获取"
        }

        override fun onFinish() {
            button.text = "获取验证码"
            button.isEnabled = true
        }
    }

}

