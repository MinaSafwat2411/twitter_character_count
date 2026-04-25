package com.twitter.twitterui.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twitter.twitterui.data.model.CreateTweetRequest
import com.twitter.twitterui.di.MainDispatcher
import com.twitter.twitterui.domain.usecase.ITwitterUseCase
import com.twitter.twitterui.domain.usecase.analyze.IAnalyzeTweetUseCase
import com.twitter.twitterui.presentation.contract.TwitterContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TwitterViewModel @Inject constructor(
    private val analyzeTweetUseCase: IAnalyzeTweetUseCase,
    private val useCase: ITwitterUseCase,
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow(TwitterContract.State())
    val state: StateFlow<TwitterContract.State> = _state.asStateFlow()

    private val _effect = Channel<TwitterContract.Effect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: TwitterContract.Event) {
        when (event) {
            is TwitterContract.Event.OnSetToken -> _state.update { it.copy(token = event.token) }
            is TwitterContract.Event.OnSetMaxLimit -> {
                _state.update { it.copy(maxLimit = event.limit) }
                updateText(_state.value.text)
            }
            is TwitterContract.Event.OnTextChanged -> updateText(event.text)
            TwitterContract.Event.OnClearClicked -> updateText("")
            TwitterContract.Event.OnPostClicked -> postTweet()
            TwitterContract.Event.OnCopyClicked -> copyText()
        }
    }

    private fun copyText() {
        viewModelScope.launch {
            _effect.send(TwitterContract.Effect.CopyTextToClipboard(_state.value.text))
        }
    }

    private fun updateText(newText: String) {
        val validation = analyzeTweetUseCase(newText, _state.value.maxLimit)
        _state.update { currentState ->
            currentState.copy(
                text = newText,
                charactersTyped = validation.charactersTyped,
                charactersRemaining = validation.charactersRemaining,
                isValid = validation.isValid
            )
        }
    }

    private fun postTweet() {
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch(mainDispatcher) {
                _effect.send(TwitterContract.Effect.ShowPostError(exception.message ?: "Unknown error"))
            }
        }

        viewModelScope.launch (mainDispatcher + handler) {
            useCase.createTweet(CreateTweetRequest(
                text = _state.value.text
            ), _state.value.token).onStart {
                _state.update { it.copy(isLoading = true) }
            }.onCompletion {
                _state.update { it.copy(isLoading = false) }
            }.catch {
                _effect.send(TwitterContract.Effect.ShowPostError(it.message ?: "Unknown error"))
            }.collect { response ->
                if (response.isSuccess()) {
                    _effect.send(TwitterContract.Effect.ShowPostSuccess)
                    _effect.send(TwitterContract.Effect.CloseScreen)
                } else {
                    _effect.send(TwitterContract.Effect.ShowPostError("Failed to post tweet: ${response.errorCode}"))
                }
            }
        }
    }
}