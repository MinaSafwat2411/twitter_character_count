package com.twitter.twitterui.domain.usecase.analyze

data class AnalyzeResult(
    val charactersTyped: Int,
    val charactersRemaining: Int,
    val isValid: Boolean,
)
