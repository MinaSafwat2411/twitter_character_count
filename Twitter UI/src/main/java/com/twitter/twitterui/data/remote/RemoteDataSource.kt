package com.twitter.twitterui.data.remote

import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.data.model.CreateTweetResponse
import com.twitter.twitterui.retrofit.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiInterface: ApiInterface
): IRemoteDataSource {
    override suspend fun createTweet(
        authorization: String,
        request: CreateTweetRequest
    ): Response<CreateTweetResponse> {
        return apiInterface.createTweet(authorization, request)
    }
}