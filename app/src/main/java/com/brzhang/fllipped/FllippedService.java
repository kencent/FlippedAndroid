package com.brzhang.fllipped;

import com.brzhang.fllipped.model.FlippedsResponse;
import com.brzhang.fllipped.model.Flippedword;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by brzhang on 2017/7/4.
 * Description :
 */

public interface FllippedService {


    @POST("flippedwords")
    Observable<Flippedword> createFllipped(Flippedword fllipped);

    @GET("nearby_flippedwords")
    Observable<FlippedsResponse> getNearByFlippeds(@QueryMap Map<String,String> querys);

}
