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

    object Errors {
        const val NO_NETWORK = "Please check your internet connection and try again."
        const val UNAUTHORIZED = "Invalid or expired token. Please log in again."
        const val FORBIDDEN = "You don't have permission to perform this action."
        const val SERVER_ERROR = "Something went wrong on our end. Please try again later."
        const val NO_DATA = "No data received from the server."
        const val UNKNOWN_ERROR = "An unexpected error occurred. Please try again."
    }

}