package com.brzhang.fllipped.api

import com.brzhang.fllipped.SignResponse
import com.brzhang.fllipped.model.FlippedsResponse
import com.brzhang.fllipped.model.Flippedword
import com.brzhang.fllipped.model.VeryCodeResponse
import okhttp3.ResponseBody
import retrofit2.http.*
import rx.Observable

/**
 *
 * Created by brzhang on 2017/7/8.
 * Description :
 */
interface FllippedService {


    @GET("password")
    fun getVeryCode(): Observable<VeryCodeResponse>

    @GET("login")
    fun login(): Observable<ResponseBody>

    @POST("flippedwords")
    fun createFllipped(@Body fllipped: Flippedword): Observable<Flippedword>

    @GET("nearby_flippedwords")
    fun getNearByFlippeds(@QueryMap querys: Map<String, String>): Observable<FlippedsResponse>

    @GET("mypub_flippedwords")
    fun getMypubFlippedwords(): Observable<FlippedsResponse>

    @GET("my_flippedwords?id=103")
    fun getReceivesFlippedwords(@Query("id") id: String): Observable<FlippedsResponse>

    @GET("flippedwords/{id}")
    fun getFlippedDetail(@Path("id") flippedId:String):Observable<Flippedword>

    @GET("youtusig")
    fun getSign(): Observable<SignResponse>


    @GET("help")
    fun getHelp(): Observable<Flippedword>

    @POST("feedbacks")
    fun feedbacks(@Body flippedword: Flippedword):Observable<ResponseBody>

}