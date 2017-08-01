package com.brzhang.fllipped.view

import android.support.v4.app.Fragment
import android.widget.Toast
import com.brzhang.fllipped.api.FllippedService
import com.brzhang.fllipped.api.RetrofitClient

/**
 *
 * Created by brzhang on 2017/7/8.
 * Description :
 */

abstract class BaseFragment: Fragment() {


    fun fllippedNetService(): FllippedService {
        return RetrofitClient.newInstance().create(FllippedService::class.java)
    }

    fun toast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }
}