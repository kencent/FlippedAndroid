package com.brzhang.fllipped

import android.app.Application
import android.content.Context

/**
 *
 * Created by brzhang on 2017/7/8.
 * Description :
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    companion object {
        internal lateinit var sInstance: App

        fun ApplicationContext(): Context {
            return sInstance.applicationContext
        }
    }
}
