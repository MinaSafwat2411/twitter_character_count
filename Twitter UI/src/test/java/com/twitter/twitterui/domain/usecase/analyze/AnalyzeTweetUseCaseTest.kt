package com.twitter.twitterui.domain.usecase.analyze

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AnalyzeTweetUseCaseTest {

    private lateinit var useCase: AnalyzeTweetUseCase

    @Before
    fun setUp() {
        useCase = AnalyzeTweetUseCase()
    }

    @Test
    fun `when text is empty, charactersTyped should be 0 and isValid should be false`() {
        val result = useCase("", 280)
        assertEquals(0, result.charactersTyped)
        assertEquals(280, result.charactersRemaining)
        assertFalse(result.isValid)
    }

    @Test
    fun `when text is within limit, isValid should be true`() {
        val result = useCase("Hello Twitter", 280)
        assertEquals(13, result.charactersTyped)
        assertEquals(267, result.charactersRemaining)
        assertTrue(result.isValid)
    }

    @Test
    fun `when text exceeds limit, isValid should be false`() {
        val maxLimit = 10
        val result = useCase("This text is too long", maxLimit)
        assertEquals(21, result.charactersTyped)
        assertEquals(-11, result.charactersRemaining)
        assertFalse(result.isValid)
    }

    @Test
    fun `when maxLimit is not 280 and text is empty, isValid should be false`() {
        val result = useCase("", 140)
        assertEquals(0, result.charactersTyped)
        assertFalse(result.isValid)
    }
}
