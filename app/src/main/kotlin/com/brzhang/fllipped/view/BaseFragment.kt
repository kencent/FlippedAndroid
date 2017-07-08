package com.brzhang.fllipped.view

import android.support.v4.app.Fragment
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
}