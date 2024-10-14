package com.example.infopersonalapp.network

import com.example.infopersonalapp.data.AccessTokenResponse
import com.example.infopersonalapp.data.UserMediaResponse
import com.example.infopersonalapp.data.UserProfile
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface InstagramApiService {
    @FormUrlEncoded
    @POST("oauth/access_token")
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String
    ): Call<AccessTokenResponse>

    @GET("me")
    fun getUserProfile(
        @Query("fields") fields: String = "id,username,account_type",
        @Query("access_token") accessToken: String
    ): Call<UserProfile>

    @GET("me/media")
    fun getUserMedia(
        @Query("fields") fields: String = "id,caption,media_type,media_url,thumbnail_url,permalink",
        @Query("access_token") accessToken: String
    ): Call<UserMediaResponse>
}
