package com.twitter.twitterui.domain.usecase.analyze

import com.twitter.twittertext.TwitterTextParser
import javax.inject.Inject

class AnalyzeTweetUseCase @Inject constructor() : IAnalyzeTweetUseCase {
	override fun invoke(text: String, maxLimit: Int): AnalyzeResult {
		val results = TwitterTextParser.parseTweet(text)
		
		val charactersTyped = results.weightedLength
		val charactersRemaining = maxLimit - charactersTyped
		
		return AnalyzeResult(
			charactersTyped = charactersTyped,
			charactersRemaining = charactersRemaining,
			isValid = if (maxLimit == 280) results.isValid else (charactersTyped in 1..maxLimit)
		)
	}
}

