package com.twitter.twitterui.data.repository

import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.data.model.CreateTweetResponse
import com.twitter.twitterui.data.model.Status
import com.twitter.twitterui.data.remote.IRemoteDataSource
import com.twitter.twitterui.di.IoDispatcher
import com.twitter.twitterui.utils.Constants.Errors
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
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
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
                emit(Status.NoNetwork<T>(error = Errors.NO_NETWORK))
                return@flow
            }

            try {
                val response = apiCall.invoke()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        emit(Status.Success<T>(body))
                    } else {
                        emit(Status.NoData<T>(error = Errors.NO_DATA))
                    }
                } else {
                    when (response.code()) {
                        401 -> emit(Status.NotAuthorized<T>(error = Errors.UNAUTHORIZED))
                        403 -> emit(Status.Forbidden<T>(error = Errors.FORBIDDEN))
                        500 -> emit(Status.ServerError<T>(error = Errors.SERVER_ERROR))
                        else -> emit(
                            Status.Error<T>(
                                error = Errors.UNKNOWN_ERROR,
                                errorCode = response.code()
                            )
                        )
                    }
                }

            } catch (throwable: Throwable) {
                when (throwable) {
                    is SocketException -> emit(Status.NoNetwork<T>(error = Errors.NO_NETWORK))
                    is HttpException -> emit(
                        Status.Error<T>(
                            error = throwable.message() ?: Errors.UNKNOWN_ERROR,
                            errorCode = throwable.code()
                        )
                    )
                    else -> emit(Status.Error<T>(error = throwable.localizedMessage ?: Errors.UNKNOWN_ERROR))
                }
            }
        }.flowOn(dispatcher)
    }
}
