package com.brzhang.fllipped.view

import android.content.Intent
import android.os.Bundle
import com.brzhang.fllipped.R
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by hoollyzhang on 2017/7/26.
 */

class SplashActivity : BaseActivity() {
    override fun handleRxEvent(event: Any?) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        activity_splash_iv.postDelayed({
            gotoMainActivity()
        }, 1500)
    }

    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }
}