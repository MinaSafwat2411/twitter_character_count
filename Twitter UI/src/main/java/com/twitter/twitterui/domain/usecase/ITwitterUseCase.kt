package com.twitter.twitterui.domain.usecase

import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.data.model.CreateTweetResponse
import com.twitter.twitterui.data.model.Status
import kotlinx.coroutines.flow.Flow

interface ITwitterUseCase {
    fun createTweet(createTweetRequest: CreateTweetRequest,token: String) : Flow<Status<CreateTweetResponse>>
}