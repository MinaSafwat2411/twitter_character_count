package com.twitter.twitterui.data.remote

import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.data.model.CreateTweetResponse
import retrofit2.Response

interface IRemoteDataSource {
    suspend fun createTweet(authorization: String, request: CreateTweetRequest): Response<CreateTweetResponse>
}