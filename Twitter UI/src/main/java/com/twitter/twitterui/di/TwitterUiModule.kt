package com.twitter.twitterui.di


import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.twitter.twitterui.data.remote.IRemoteDataSource
import com.twitter.twitterui.data.remote.RemoteDataSource
import com.twitter.twitterui.data.repository.ITwitterRepository
import com.twitter.twitterui.data.repository.TwitterRepository
import com.twitter.twitterui.domain.usecase.ITwitterUseCase
import com.twitter.twitterui.domain.usecase.TwitterUseCase
import com.twitter.twitterui.retrofit.ApiInterface
import com.twitter.twitterui.utils.connection_utils.ConnectionUtils
import com.twitter.twitterui.utils.connection_utils.IConnectionUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }

    @Provides
    @Singleton
    fun provideConnectivity(@ApplicationContext context: Context): IConnectionUtils {
        return ConnectionUtils(context)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiInterface: ApiInterface,
    ): IRemoteDataSource {
        return RemoteDataSource(apiInterface)
    }

    @Provides
    @IoDispatcher
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTwitterRepository(
        twitterRepository: TwitterRepository
    ): ITwitterRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindTwitterUseCase(
        twitterUseCase: TwitterUseCase
    ): ITwitterUseCase
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher
