package com.twitter.twitterui.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CreateTweetRequest(
    @SerialName("card_uri")
    val cardUri: String? = null,
    @SerialName("community_id")
    val communityId: String? = null,
    @SerialName("direct_message_deep_link")
    val directMessageDeepLink: String? = null,
    @SerialName("for_super_followers_only")
    val forSuperFollowersOnly: Boolean? = null,
    val geo: GeoRequest? = null,
    @SerialName("made_with_ai")
    val madeWithAi: Boolean? = null,
    val nullcast: Boolean? = null,
    @SerialName("paid_partnership")
    val paidPartnership: Boolean? = null,
    @SerialName("quote_tweet_id")
    val quoteTweetId: String? = null,
    @SerialName("reply_settings")
    val replySettings: String? = null,
    @SerialName("share_with_followers")
    val shareWithFollowers: Boolean? = null,
    val text: String,
)
