package com.brzhang.fllipped.view

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.brzhang.fllipped.BuildConfig
import com.brzhang.fllipped.R
import com.brzhang.fllipped.RxBus
import com.brzhang.fllipped.busevent.UserAuthFailed
import com.brzhang.fllipped.pref.UserPref
import com.brzhang.fllipped.utils.LogUtil
import com.tbruyelle.rxpermissions.RxPermissions
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.stat.StatService
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import rx.subscriptions.CompositeSubscription
import com.tencent.map.geolocation.TencentLocationRequest
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocation


/**
 *
 * Created by brzhang on 2017/7/6.
 * Description :
 */
abstract class BaseActivity : AppCompatActivity(), TencentLocationListener {
    var needLocation = false
    val mSubScription = CompositeSubscription()
    var rxPermissions: RxPermissions? = null

    var mLocation: TencentLocation? = null
    var mLocationManager: TencentLocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBusEvent()
        rxPermissions = RxPermissions(this)
        mLocationManager = TencentLocationManager.getInstance(this)
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        //mLocationManager?.coordinateType = TencentLocationManager.COORDINATE_TYPE_GCJ02
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (needLocation) {
            rxPermissions?.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ?.subscribe { grant ->
                        if (grant) {
                            startLocation()
                        } else {
                            toast("请到权限中开启定位授权")
                        }
                    }
        }
    }

    private fun startLocation() {
        if (needLocation){
            val request = TencentLocationRequest.create()
            request.interval = 60* 1000
            var error = mLocationManager?.requestLocationUpdates(request, this)
            LogUtil.dLoge("hoolly","tencent map update error $error")
        }
    }

    private fun stopLocation() {
        mLocationManager?.removeUpdates(this)
    }

    override fun onLocationChanged(p0: TencentLocation?, p1: Int, p2: String?) {

        if (TencentLocation.ERROR_OK == p1) {
            // 定位成功
            mLocation = p0
            UserPref.setUserLocation(this,mLocation)
            dToast("定位成功")
        } else {
            // 定位失败
            dToast("定位失败,$p2")
        }

    }

    override fun onStatusUpdate(p0: String?, p1: Int, p2: String?) {

    }

    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
        stopLocation()
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
                    //UserPref.setUserLogin(this, false)
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