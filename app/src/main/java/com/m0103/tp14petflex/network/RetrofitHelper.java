package com.m0103.tp14petflex.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {

    public static Retrofit getRetrofitInstance(String baseUrl){

        Retrofit.Builder builder=new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.addConverterFactory(ScalarsConverterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();

        return retrofit;
    }

}
