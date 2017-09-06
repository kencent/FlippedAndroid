package com.brzhang.fllipped.api

import com.brzhang.fllipped.model.*
import okhttp3.ResponseBody
import retrofit2.Response
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

    @DELETE("/flippedwords/{id}")
    fun deleteFllipped(@Path("id") id: Int): Observable<Response<Void>>

    @GET("nearby_flippedwords")
    fun getNearByFlippeds(@QueryMap querys: Map<String, String>): Observable<FlippedsResponse>

    @GET("mypub_flippedwords")
    fun getMypubFlippedwords(@Query("id") id: String): Observable<FlippedsResponse>

    @GET("my_flippedwords")
    fun getReceivesFlippedwords(@QueryMap querys: Map<String, String>): Observable<FlippedsResponse>

    @GET("flippedwords/{id}")
    fun getFlippedDetail(@Path("id") flippedId: String): Observable<Flippedword>

    @GET("youtusig")
    fun getSign(): Observable<SignResponse>

    @GET("ilvbsig")
    fun getLvbSig(): Observable<SignResponse>

    @POST("flippedcalls")
    fun callSomeOne(@Body callRequeset: CallRequeset): Observable<CallResponse>

    @DELETE("flippedcalls")
    fun cancelCall(): Observable<ResponseBody>

    @GET("help")
    fun getHelp(): Observable<Flippedword>

    @POST("feedbacks")
    fun feedbacks(@Body flippedword: Flippedword): Observable<ResponseBody>

    @POST("flippedwords/{id}/comments")
    fun comment(@Path("id") id: String, @Body comment: Comment): Observable<Comment>

    @GET("flippedwords/{id}/comments")
    fun listComments(@Path("id") id: String): Observable<CommentListResponse>

    @DELETE("flippedwords/{id}/comments/{commentid}")
    fun deleteComment(@Path("id") id: Int, @Path("commentid") commentid: Int): Observable<Response<Void>>

}