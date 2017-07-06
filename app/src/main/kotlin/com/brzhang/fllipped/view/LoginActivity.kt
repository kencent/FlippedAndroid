package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.brzhang.fllipped.R

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : FlippedBaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_login
    }

    override fun setupView(view: View) {
//        view.findViewById()
        hideRight()
        hideNavigationBack()
        setActTitle("登录")
    }
}

