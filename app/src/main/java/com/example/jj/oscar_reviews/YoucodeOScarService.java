package com.example.jj.oscar_reviews;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YoucodeOScarService {
//    @GET("Lab01Servlet")
//    Call<String> listJSONServlet(@Query("CATEGORY") String sort);

    @GET("{path}")
    Call<String> listJSONServlet(@Path("path") String pathUrl, @Query("CATEGORY") String sort);
//    Call<String> listJSONServlet();

//    @FormUrlEncoded
//    @POST("Lab01Servlet")
//    Call<String> postReview(@Field("REVIEW") String review, @Field("REVIEWER") String reviewer,
//                            @Field("NOMINEE") String nominee, @Field("CATEGORY") String category,
//                            @Field("PASSWORD") String password);
    @FormUrlEncoded
    @POST("{path}")
    Call<String> postReview(@Path("path") String pathUrl, @Field("REVIEW") String review, @Field("REVIEWER") String reviewer,
                            @Field("NOMINEE") String nominee, @Field("CATEGORY") String category,
                            @Field("PASSWORD") String password);
}
