package com.twitter.twitterui.utils

import com.twitter.twitterui.BuildConfig

object Constants {
    object Network {
        const val CONNECT_TIMEOUT = 5L
        const val READ_TIMEOUT = 5L
        const val WRITE_TIMEOUT = 5L
    }

    object URL {
        const val BASE_URL = BuildConfig.BASE_NETWORK_URL
    }

    object Headers {
        const val AUTHORIZATION = "Authorization"
        const val CONTENT_TYPE = "Content-Type"
        const val ACCEPT_JSON_VALUE = "application/json"
    }

}