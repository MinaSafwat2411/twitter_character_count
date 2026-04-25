package com.twitter.twitterui.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TweetData(
    @SerialName("id")
    val id: String? = null,
    @SerialName("text")
    val text: String? = null,
)