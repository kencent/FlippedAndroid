package com.brzhang.fllipped.api

import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
import com.brzhang.fllipped.model.VeryCodeResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import rx.Observable

/**
 *
 * Created by brzhang on 2017/7/8.
 * Description :
 */
interface FllippedService {


    @GET("password")
    fun getVeryCode(@Query("phone") phone: String): Observable<VeryCodeResponse>

    @GET("login")
    fun login(): Observable<ResponseBody>

    @POST("flippedwords")
    fun createFllipped(fllipped: Flippedword): Observable<Flippedword>

    @GET("nearby_flippedwords")
    fun getNearByFlippeds(@QueryMap querys: Map<String, String>): Observable<FlippedsResponse>

}