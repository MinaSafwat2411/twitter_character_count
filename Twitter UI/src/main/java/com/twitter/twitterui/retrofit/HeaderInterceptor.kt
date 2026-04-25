package com.twitter.twitterui.retrofit

import android.content.Context
import com.twitter.twitterui.utils.Constants.Headers.ACCEPT_JSON_VALUE
import com.twitter.twitterui.utils.Constants.Headers.CONTENT_TYPE
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    @ApplicationContext val context: Context,
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        builder.addHeader(CONTENT_TYPE, ACCEPT_JSON_VALUE)

        val request = builder.build()
        return chain.proceed(request)
    }
}
