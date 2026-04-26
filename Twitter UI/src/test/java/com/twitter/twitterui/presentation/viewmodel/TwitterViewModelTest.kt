package com.twitter.twitterui.presentation.viewmodel

import app.cash.turbine.test
import com.twitter.twitterui.data.model.CreateTweetResponse
import com.twitter.twitterui.data.model.Status
import com.twitter.twitterui.domain.usecase.ITwitterUseCase
import com.twitter.twitterui.domain.usecase.analyze.AnalyzeResult
import com.twitter.twitterui.domain.usecase.analyze.IAnalyzeTweetUseCase
import com.twitter.twitterui.presentation.contract.TwitterContract
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TwitterViewModelTest {

    private lateinit var viewModel: TwitterViewModel
    private val analyzeTweetUseCase: IAnalyzeTweetUseCase = mockk()
    private val twitterUseCase: ITwitterUseCase = mockk()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        // Default mock behavior
        every { analyzeTweetUseCase(any(), any()) } returns AnalyzeResult(0, 280, true)
        
        viewModel = TwitterViewModel(
            analyzeTweetUseCase = analyzeTweetUseCase,
            useCase = twitterUseCase,
            mainDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when OnTextChanged event is received, state should be updated`() = runTest {
        val text = "Hello"
        every { analyzeTweetUseCase(text, 280) } returns AnalyzeResult(5, 275, true)

        viewModel.onEvent(TwitterContract.Event.OnTextChanged(text))
        testDispatcher.scheduler.runCurrent()

        assertEquals(text, viewModel.state.value.text)
        assertEquals(5, viewModel.state.value.charactersTyped)
        assertEquals(275, viewModel.state.value.charactersRemaining)
    }

    @Test
    fun `when OnSetMaxLimit event is received, state should be updated and text re-analyzed`() = runTest {
        val limit = 140
        every { analyzeTweetUseCase("", limit) } returns AnalyzeResult(0, 140, true)

        viewModel.onEvent(TwitterContract.Event.OnSetMaxLimit(limit))
        testDispatcher.scheduler.runCurrent()

        assertEquals(limit, viewModel.state.value.maxLimit)
        assertEquals(140, viewModel.state.value.charactersRemaining)
    }

    @Test
    fun `when OnClearClicked event is received, text should be cleared`() = runTest {
        every { analyzeTweetUseCase("", 280) } returns AnalyzeResult(0, 280, true)

        viewModel.onEvent(TwitterContract.Event.OnClearClicked)
        testDispatcher.scheduler.runCurrent()

        assertEquals("", viewModel.state.value.text)
    }

    @Test
    fun `when OnCopyClicked event is received, CopyTextToClipboard effect should be sent`() = runTest {
        val text = "Copy me"
        every { analyzeTweetUseCase(text, 280) } returns AnalyzeResult(7, 273, true)
        viewModel.onEvent(TwitterContract.Event.OnTextChanged(text))
        testDispatcher.scheduler.runCurrent()

        viewModel.effect.test {
            viewModel.onEvent(TwitterContract.Event.OnCopyClicked)
            val effect = awaitItem()
            assert(effect is TwitterContract.Effect.CopyTextToClipboard)
            assertEquals(text, (effect as TwitterContract.Effect.CopyTextToClipboard).text)
        }
    }

    @Test
    fun `when OnPostClicked is successful, CloseScreen effect should be sent`() = runTest {
        val text = "Tweet text"
        val token = "token"
        every { analyzeTweetUseCase(text, 280) } returns AnalyzeResult(text.length, 280 - text.length, true)
        viewModel.onEvent(TwitterContract.Event.OnSetToken(token))
        viewModel.onEvent(TwitterContract.Event.OnTextChanged(text))
        testDispatcher.scheduler.runCurrent()

        every { twitterUseCase.createTweet(any(), token) } returns flowOf(Status.Success(CreateTweetResponse()))

        viewModel.effect.test {
            viewModel.onEvent(TwitterContract.Event.OnPostClicked)
            
            assertEquals(TwitterContract.Effect.ShowPostSuccess, awaitItem())
            assertEquals(TwitterContract.Effect.CloseScreen, awaitItem())
        }
    }
}
