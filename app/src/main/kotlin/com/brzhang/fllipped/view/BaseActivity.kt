package com.brzhang.fllipped.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 *
 * Created by brzhang on 2017/7/6.
 * Description :
 */
abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }
}