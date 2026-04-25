package com.twitter.twitterui.data.repository

import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.data.model.CreateTweetResponse
import com.twitter.twitterui.data.model.Status
import kotlinx.coroutines.flow.Flow

interface ITwitterRepository {
    fun postTweet(createTweetRequest: CreateTweetRequest,token: String) : Flow<Status<CreateTweetResponse>>
}