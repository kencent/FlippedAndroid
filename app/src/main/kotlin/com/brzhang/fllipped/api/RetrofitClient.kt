package com.brzhang.fllipped.api

import android.util.Base64
import com.brzhang.fllipped.App
import com.brzhang.fllipped.RxBus
import com.brzhang.fllipped.busevent.UserAuthFailed
import com.brzhang.fllipped.pref.UserPref
import com.brzhang.fllipped.utils.EncodeUtils
import com.brzhang.fllipped.utils.LogUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 *
 * Created by brzhang on 2017/7/8.
 * Description :
 */
object RetrofitClient {
    fun newInstance(username: String = "", salt: String = ""): Retrofit {

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            var username = username
            var salt = salt

            val uri = chain.request().url().query()

            if (username.isEmpty()) {
                username = UserPref.getUserName(App.ApplicationContext(), "")
            }
            if (salt.isEmpty()) {
                salt = UserPref.getUserSalt(App.ApplicationContext(), "")
            }

            var password = UserPref.getUserPassWord(App.ApplicationContext(), "")

            LogUtil.dLoge("hoolly", "username is [$username]")
            LogUtil.dLoge("hoolly", "salt is [$salt]")
            val token = getAuthorization(chain, username, salt, password)
            val request = original.newBuilder()
                    .header("Authorization", token)
                    .header("x-uid:", username)
                    .method(original.method(), original.body())
                    .build()
            var response = chain.proceed(request)
            if (response.code() == 401 && !uri.contains("login",false)){
                RxBus.getRxBusSingleton().send(UserAuthFailed())
            }
            response
        }
        val retrofit = Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl("https://flippedwords.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        return retrofit

    }

    private fun getAuthorization(chain: Interceptor.Chain, username: String, salt: String, password: String): String {
        val ts = System.currentTimeMillis()
        val rd = Random().nextInt()
        val method = chain.request().method()
        val uri = chain.request().url().query()
        val body = chain.request().body()?.toString() ?: ""

        LogUtil.dLoge("hoolly", "request body is [$body]")
        // step 1 key = md5(phone + md5(password + salt))
        val key = EncodeUtils.md5(username + EncodeUtils.md5(salt + password))
        // step 2 signature = base64(hmac_sha1(key, username + ts + rd + method + uri + body))
        val data = username + ts + rd + method + uri + body
        val signature = Base64.encodeToString(EncodeUtils.hamcsha1(key.toByteArray(), data.toByteArray())?.toByteArray(), Base64.NO_WRAP)
        LogUtil.dLoge("hoolly", "signature is [$signature]")
        // step 3 token = base64(json_encode({"ts": ts, "rd": rd, "sign": signature}))
        var tokenJson = JSONObject()
        tokenJson.put("ts", ts)
        tokenJson.put("rd", rd)
        tokenJson.put("sign", signature)
        val token = Base64.encodeToString(tokenJson.toString().toByteArray(), Base64.NO_WRAP)
        LogUtil.dLoge("hoolly", "token is [$token]")
        return token
    }
}