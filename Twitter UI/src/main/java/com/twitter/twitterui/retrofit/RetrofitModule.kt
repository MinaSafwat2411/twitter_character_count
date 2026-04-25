package com.twitter.twitterui.retrofit

import android.content.Context
import com.twitter.twitterui.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {


    @Provides
    @Singleton
    fun provideOkHttp(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
    ): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .connectTimeout(Constants.Network.CONNECT_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(Constants.Network.READ_TIMEOUT, TimeUnit.MINUTES)
            .writeTimeout(Constants.Network.WRITE_TIMEOUT, TimeUnit.MINUTES)

        okhttp.addInterceptor(headerInterceptor)
        okhttp.addInterceptor(httpLoggingInterceptor)

        return okhttp.build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return httpLoggingInterceptor
    }

    @Provides
    fun provideHeaderInterceptor(@ApplicationContext context: Context): HeaderInterceptor =
        HeaderInterceptor(context)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.URL.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}
