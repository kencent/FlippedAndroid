package com.brzhang.fllipped;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by brzhang on 2017/7/4.
 * Description :
 */

public class RetrofitClient {
    public static Retrofit newInstance() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", "fuck you")
                        .header("x-uid:", "18681446372")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl("https://flippedwords.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;

    }
}
