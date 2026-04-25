package com.twitter.twitterui.domain.usecase

import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.data.model.CreateTweetResponse
import com.twitter.twitterui.data.model.Status
import com.twitter.twitterui.data.repository.ITwitterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TwitterUseCase @Inject constructor(
    private val repository: ITwitterRepository
) : ITwitterUseCase {
    override fun createTweet(
        createTweetRequest: CreateTweetRequest,
        token: String
    ): Flow<Status<CreateTweetResponse>> {
        return repository.postTweet(createTweetRequest, token).map { response ->
            when (val validated = onValidateResponse(response)) {
                is Status.Success -> {
                    Status.Success(validated.data)
                }
                is Status.Error -> {
                    Status.Error(validated.data, validated.error ?: "An error occurred", validated.errorCode ?: 0)
                }
                else -> {
                    validated
                }
            }
        }
    }

    private fun <T> onValidateResponse(response: Status<T>): Status<T> {
        return if (response is Status.Success && response.data == null) {
            Status.Error(error = "Response data is null")
        } else {
            response
        }
    }
}
