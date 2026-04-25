package com.twitter.twitterui.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoRequest(
    @SerialName("place_id")
    val placeId: String,
)
