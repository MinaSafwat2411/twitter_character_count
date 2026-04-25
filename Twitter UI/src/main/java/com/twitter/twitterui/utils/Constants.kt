package com.twitter.twitterui.utils

import com.twitter.twitterui.BuildConfig

object Constants {
    object SharedPreference {
        const val SHARED_PREF_NAME = "my_shared_pref"
    }

    object General {

    }

    object Errors {

    }

    object NavArguments {}

    object ComposeConstants {}


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

    object Params {

    }

}