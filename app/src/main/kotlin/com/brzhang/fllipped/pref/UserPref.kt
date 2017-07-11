package com.brzhang.fllipped.pref

import android.content.Context
import com.brzhang.fllipped.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tencent.map.geolocation.TencentLocation
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * Created by brzhang on 2017/7/8.
 * Description :
 */
object UserPref {
    val PREF_NAME = "user_pref"

    val KEY_USER_NAME = "username"
    val KEY_SALT = "usersalt"
    val KEY_PASSWORD = "userpassword"
    val KEY_IS_USER_LOGIN = "islogin"

    var KEY_USER_LOCATION = "key_user_location"

    fun getString(ctx: Context, key: String, defaultValue: String): String {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        return sp.getString(key, defaultValue)
    }

    fun setString(ctx: Context, key: String, value: String) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().putString(key, value).apply()
    }

    fun getBoolean(ctx: Context, key: String, defaultValue: Boolean): Boolean {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        return sp.getBoolean(key, defaultValue)
    }

    fun setBoolean(ctx: Context, key: String, value: Boolean) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().putBoolean(key, value).apply()
    }


    fun getUserName(context: Context, defaultValue: String): String {
        return getString(context, KEY_USER_NAME, defaultValue)
    }

    fun setUserName(context: Context, value: String) {
        setString(context, KEY_USER_NAME, value)
    }

    fun getUserSalt(context: Context, defaultValue: String): String {
        return getString(context, KEY_SALT, defaultValue)
    }

    fun setUserSalt(context: Context, value: String) {
        setString(context, KEY_SALT, value)
    }

    fun getUserPassWord(context: Context, defaultValue: String): String {
        return getString(context, KEY_PASSWORD, defaultValue)
    }

    fun setUserPassWord(context: Context, value: String) {
        setString(context, KEY_PASSWORD, value)
    }

    fun isUserLogin(context: Context, defaultValue: Boolean = false): Boolean {
        return getBoolean(context, KEY_IS_USER_LOGIN, defaultValue)
    }

    fun setUserLogin(context: Context, userLogin: Boolean) {
        setBoolean(context, KEY_IS_USER_LOGIN, userLogin)
    }

    fun setUserLocation(context: Context, mLocation: TencentLocation?) {
        var latlng = LatLng()
        latlng.lat = mLocation?.latitude
        latlng.lng = mLocation?.longitude
        setString(context,KEY_USER_LOCATION,Gson().toJson(latlng).toString())
    }

    fun getUserLocation(context: Context):LatLng?{
        var latlngStr = getString(context, KEY_USER_LOCATION,"{}")
        if (latlngStr.equals("{}")){
            return null
        }else{
            var latLng = Gson().fromJson(latlngStr,LatLng::class.java)
            return latLng
        }
    }

}