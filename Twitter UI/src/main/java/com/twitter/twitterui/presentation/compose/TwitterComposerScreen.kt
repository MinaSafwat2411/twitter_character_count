package com.twitter.twitterui.presentation.compose

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.twitter.twitterui.R
import com.twitter.twitterui.presentation.contract.TwitterContract
import com.twitter.twitterui.presentation.viewmodel.TwitterViewModel

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
    borderStrokeColor: Color = Color(0xFFE0E0E0),
    titleText: String = "Twitter character count",
    placeholderText: String = "Start typing! You can enter up to characters",
    onBack: () -> Unit = {}
) {
    val viewModel : TwitterViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    
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

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TwitterContract.Effect.ShowPostSuccess -> {
                    Toast.makeText(context, "Tweet posted successfully!", Toast.LENGTH_SHORT).show()
                }
                is TwitterContract.Effect.ShowPostError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
                is TwitterContract.Effect.CloseScreen -> {
                    onBack()
                }
                is TwitterContract.Effect.CopyTextToClipboard -> {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Tweet", effect.text)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_media_play), 
                            contentDescription = "Next",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor
                )
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
            Icon(
                painter = painterResource(id = R.drawable.ic_twitter_logo),
                contentDescription = "Twitter Logo",
                tint = twitterBlue,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CountCard(
                    title = "Characters Typed",
                    value = "${state.charactersTyped}/${state.maxLimit}",
                    modifier = Modifier.weight(1f),
                    headerColor = cardBackgroundColor,
                    borderColor = borderStrokeColor
                )
                CountCard(
                    title = "Characters Remaining",
                    value = state.charactersRemaining.toString(),
                    modifier = Modifier.weight(1f),
                    headerColor = cardBackgroundColor,
                    borderColor = borderStrokeColor
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.text,
                onValueChange = { viewModel.onEvent(TwitterContract.Event.OnTextChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = { Text(dynamicPlaceholder, color = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = borderStrokeColor,
                    unfocusedBorderColor = borderStrokeColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.onEvent(TwitterContract.Event.OnCopyClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = copyButtonColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Copy Text", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.onEvent(TwitterContract.Event.OnClearClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = clearButtonColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear Text", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onEvent(TwitterContract.Event.OnPostClicked) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = twitterBlue),
                shape = RoundedCornerShape(12.dp),
                enabled = state.isValid && state.text.isNotEmpty() && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Post tweet", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CountCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    headerColor: Color,
    borderColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
