package com.brzhang.fllipped.pref

import android.content.Context

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

}