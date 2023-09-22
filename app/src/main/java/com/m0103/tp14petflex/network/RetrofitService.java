package com.m0103.tp14petflex.network;

import com.m0103.tp14petflex.data.KakaoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitService {

    //키워드 장소검색
    @Headers({"Authorization: KakaoAK ca9c1a5f07beafbc1a0a0f9a12d3b2ec"})
    @GET("/v2/local/search/keyword.json?radius=15000&sort=distance")
    Call<KakaoResponse> searchPlaceKakao(@Query("query") String searchQuery,
                                         @Query("x") String longitude,
                                         @Query("y") String latitude,
                                         @Query("page") int pageNumber);
}
