package com.twitter.twitterui.retrofit

import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.data.model.CreateTweetResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("2/tweets")
    suspend fun createTweet(
        @Header("Authorization") authorization: String,
        @Body request: CreateTweetRequest,
    ): Response<CreateTweetResponse>
}