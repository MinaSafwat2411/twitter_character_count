package com.twitter.twitterui.data.repository

import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.data.model.CreateTweetResponse
import com.twitter.twitterui.data.model.Status
import com.twitter.twitterui.data.remote.IRemoteDataSource
import com.twitter.twitterui.di.IoDispatcher
import com.twitter.twitterui.utils.connection_utils.IConnectionUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketException
import javax.inject.Inject

class TwitterRepository @Inject constructor(
    private val connectionUtils: IConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ITwitterRepository {
    override fun postTweet(
        createTweetRequest: CreateTweetRequest,
        token: String,
    ): Flow<Status<CreateTweetResponse>> {
        return safeApiCalls {
            mIRemoteDataSource.createTweet(
                authorization = "Bearer $token",
                request = createTweetRequest
            )
        }
    }

    fun <T> safeApiCalls(
        apiCall: suspend () -> Response<T>,
    ): Flow<Status<T>> {
        return flow {
            if (!connectionUtils.isConnected) {
                emit(Status.NoNetwork<T>(error = "No Network"))
                return@flow
            }

            try {
                val response = apiCall.invoke()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        emit(Status.Success<T>(body))
                    } else {
                        emit(Status.NoData<T>(error = "No Data"))
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown Error"
                    when (response.code()) {
                        401 -> emit(Status.NotAuthorized<T>(error = errorMsg))
                        403 -> emit(Status.Forbidden<T>(error = errorMsg))
                        500 -> emit(Status.ServerError<T>(error = errorMsg))
                        else -> emit(Status.Error<T>(error = errorMsg, errorCode = response.code()))
                    }
                }

            } catch (throwable: Throwable) {
                when (throwable) {
                    is SocketException -> emit(Status.NoNetwork<T>(error = "Network error"))
                    is HttpException -> emit(Status.Error<T>(error = throwable.message(), errorCode = throwable.code()))
                    else -> emit(Status.Error<T>(error = throwable.localizedMessage ?: "Unknown Error"))
                }
            }
        }.flowOn(dispatcher)
    }
}
