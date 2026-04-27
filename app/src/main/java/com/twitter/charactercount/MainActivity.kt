package com.twitter.charactercount

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.twitter.charactercount.ui.theme.TwitterCharacterCountTheme
import com.twitter.twitterui.presentation.compose.TwitterComposerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TwitterCharacterCountTheme {
                TwitterComposerScreen(onBack = { finish() })
            }
        }
    }
}