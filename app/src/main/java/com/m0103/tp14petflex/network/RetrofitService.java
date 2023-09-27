package com.m0103.tp14petflex.network;

import com.m0103.tp14petflex.data.BoardData;
import com.m0103.tp14petflex.data.KakaoResponse;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitService {

    //키워드 장소 검색
    @Headers({"Authorization: KakaoAK ca9c1a5f07beafbc1a0a0f9a12d3b2ec"})
    @GET("/v2/local/search/keyword.json?radius=15000&sort=distance")
    Call<KakaoResponse> searchPlaceKakao(@Query("query") String searchQuery,
                                         @Query("x") String longitude,
                                         @Query("y") String latitude,
                                         @Query("page") int pageNumber);

    //아이디 중복 확인
    @GET("join/idCheck.php")
    Call<String> idCheck(@Query("id") String id);


    //닉네임 중복 확인
    @GET("join/nicknameCheck.php")
    Call<String> nicknameCheck(@Query("nickname") String nickname);


    //가입정보 서버로 보내기
    @FormUrlEncoded
    @POST("join/join.php")
    Call<String> join(@Field("nickname") String nickname,
                      @Field("id") String id,
                      @Field("password") String password);

    //로그인 아이디 비밀번호 확인
    @FormUrlEncoded
    @POST("join/login.php")
    Call<String> login(@Field("id") String id,
                      @Field("password") String password);


    //게시물 서버 전송
    @Multipart
    @POST("board/upload.php")
    Call<String> upload(@PartMap Map<String, String> data,
                        @Part MultipartBody.Part part);


    //게시물 데이터 가져오기
    @GET("board/load.php")
    Call<ArrayList<BoardData>> load();


    //좋아요 누름
    @GET("board/favorite.php")
    Call<String> favorite(@Query("board_no") String board_no,
                          @Query("nickname") String nickname);


    //좋아요 취소
    @GET("board/favoriteCancel.php")
    Call<String> favoriteCancel(@Query("board_no") String board_no,
                                @Query("nickname") String nickname);


    //좋아요 수 가져오기
    @GET("board/countFavorite.php")
    Call<String> countFavorite(@Query("board_no") String board_no);


    //회원 별 좋아요 기억하기
    @GET("board/setIsFav.php")
    Call<String[]> setIsFav(@Query("nickname") String nickname);
}
