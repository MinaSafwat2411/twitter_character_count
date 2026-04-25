package com.twitter.twitterui.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTweetResponse(
    @SerialName("data")
    val data: TweetData? = null,
)
