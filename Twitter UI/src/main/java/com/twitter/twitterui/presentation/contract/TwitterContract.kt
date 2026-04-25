package com.twitter.twitterui.presentation.contract


class TwitterContract {
    data class State(
        val text: String = "",
        val charactersTyped: Int = 0,
        val charactersRemaining: Int = 280,
        val maxLimit: Int = 280,
        val isValid: Boolean = true,
        val isLoading: Boolean = false,
        val token : String = "",
    )

    sealed interface Event {
        data class OnSetToken(val token: String) : Event
        data class OnSetMaxLimit(val limit: Int) : Event
        data class OnTextChanged(val text: String) : Event
        object OnClearClicked : Event
        object OnPostClicked : Event
        object OnCopyClicked : Event
    }

    sealed interface Effect {
        object ShowPostSuccess : Effect
        data class ShowPostError(val message: String) : Effect
        object CloseScreen : Effect
        data class CopyTextToClipboard(val text: String) : Effect
    }
}