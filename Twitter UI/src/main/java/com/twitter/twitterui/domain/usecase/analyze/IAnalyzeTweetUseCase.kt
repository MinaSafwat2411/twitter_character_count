package com.twitter.twitterui.domain.usecase.analyze

interface IAnalyzeTweetUseCase {
    operator fun invoke(text: String, maxLimit: Int = 280): AnalyzeResult
}
