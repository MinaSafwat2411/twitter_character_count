package com.twitter.twitterui.presentation.compose

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.twitter.twitterui.R
import com.twitter.twitterui.presentation.compose.components.*
import com.twitter.twitterui.presentation.contract.TwitterContract
import com.twitter.twitterui.presentation.viewmodel.TwitterViewModel
import java.util.Locale

@SuppressLint("LocalContextConfigurationRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitterComposerScreen(
    token: String = "",
    maxLimit: Int = 280,
    twitterBlue: Color = Color(0xFF1DA1F2),
    copyButtonColor: Color = Color(0xFF00A65A),
    clearButtonColor: Color = Color(0xFFD32F2F),
    cardBackgroundColor: Color = Color(0xFFE1F5FE),
    backgroundColor: Color = Color.White,
    borderStrokeColor: Color = Color(0xFFE1F5FE),
    titleText: String? = null,
    placeholderText: String? = null,
    onBack: () -> Unit = {},
    backIcon: Int = R.drawable.ic_back_arrow,
    topBarTextColor: Color = Color.Black,
    topBarIconColor: Color = Color.Black,
    lang: String = "en"
) {
    val viewModel: TwitterViewModel = hiltViewModel()
    
    val context = LocalContext.current
    val configuration = context.resources.configuration
    
    val localizedContext = remember(lang) {
        val locale = Locale.forLanguageTag(lang)
        val config = android.content.res.Configuration(configuration)
        config.setLocale(locale)
        context.createConfigurationContext(config)
    }

    val layoutDirection = if (lang == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalLayoutDirection provides layoutDirection
    ) {
        TwitterComposerContent(
            viewModel = viewModel,
            token = token,
            maxLimit = maxLimit,
            twitterBlue = twitterBlue,
            copyButtonColor = copyButtonColor,
            clearButtonColor = clearButtonColor,
            cardBackgroundColor = cardBackgroundColor,
            backgroundColor = backgroundColor,
            borderStrokeColor = borderStrokeColor,
            titleText = titleText ?: stringResource(id = R.string.twitter_character_count_title),
            placeholderText = placeholderText ?: stringResource(id = R.string.placeholder_text),
            onBack = onBack,
            backIcon = backIcon,
            topBarTextColor = topBarTextColor,
            topBarIconColor = topBarIconColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TwitterComposerContent(
    viewModel: TwitterViewModel,
    token: String,
    maxLimit: Int,
    twitterBlue: Color,
    copyButtonColor: Color,
    clearButtonColor: Color,
    cardBackgroundColor: Color,
    backgroundColor: Color,
    borderStrokeColor: Color,
    titleText: String,
    placeholderText: String,
    onBack: () -> Unit,
    backIcon: Int,
    topBarTextColor: Color,
    topBarIconColor: Color,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    BackHandler(onBack = onBack)

    val dynamicPlaceholder = if (placeholderText.contains("up to characters")) {
        placeholderText.replace("up to characters", "up to ${state.maxLimit} characters")
    } else {
        placeholderText
    }

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.onEvent(TwitterContract.Event.OnSetToken(token))
        }
    }

    LaunchedEffect(maxLimit) {
        viewModel.onEvent(TwitterContract.Event.OnSetMaxLimit(maxLimit))
    }

    val postSuccessMsg = stringResource(id = R.string.tweet_posted_success)
    val copiedMsg = stringResource(id = R.string.copied_to_clipboard)
    val clipboardLabel = stringResource(id = R.string.clipboard_label_tweet)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TwitterContract.Effect.ShowPostSuccess -> {
                    Toast.makeText(context, postSuccessMsg, Toast.LENGTH_SHORT).show()
                }
                is TwitterContract.Effect.ShowPostError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
                is TwitterContract.Effect.CloseScreen -> {
                    onBack()
                }
                is TwitterContract.Effect.CopyTextToClipboard -> {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(clipboardLabel, effect.text)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, copiedMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TwitterTopBar(
                title = titleText,
                onBack = onBack,
                backIcon = backIcon,
                backgroundColor = backgroundColor,
                textColor = topBarTextColor,
                iconColor = topBarIconColor
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TwitterLogo(
                twitterBlue = twitterBlue
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CountCard(
                    title = stringResource(id = R.string.characters_typed),
                    value = "${state.charactersTyped}/${state.maxLimit}",
                    modifier = Modifier.weight(1f),
                    headerColor = cardBackgroundColor,
                    borderColor = borderStrokeColor
                )
                CountCard(
                    title = stringResource(id = R.string.characters_remaining),
                    value = state.charactersRemaining.toString(),
                    modifier = Modifier.weight(1f),
                    headerColor = cardBackgroundColor,
                    borderColor = borderStrokeColor
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TwitterInputField(
                value = state.text,
                onValueChange = { viewModel.onEvent(TwitterContract.Event.OnTextChanged(it)) },
                placeholder = dynamicPlaceholder,
                borderColor = borderStrokeColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TwitterActionButton(
                    text = stringResource(id = R.string.copy_text),
                    onClick = { viewModel.onEvent(TwitterContract.Event.OnCopyClicked) },
                    containerColor = copyButtonColor,
                    modifier = Modifier.weight(1f)
                )

                TwitterActionButton(
                    text = stringResource(id = R.string.clear_text),
                    onClick = { viewModel.onEvent(TwitterContract.Event.OnClearClicked) },
                    containerColor = clearButtonColor,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TwitterPostButton(
                onClick = { viewModel.onEvent(TwitterContract.Event.OnPostClicked) },
                isEnabled = state.isValid && state.text.isNotEmpty(),
                isLoading = state.isLoading,
                twitterBlue = twitterBlue,
                text = stringResource(id = R.string.post_tweet),
            )
        }
    }
}
