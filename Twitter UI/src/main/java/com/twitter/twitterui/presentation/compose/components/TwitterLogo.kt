package com.twitter.twitterui.presentation.compose.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.twitter.twitterui.R

@Composable
fun TwitterLogo(
    twitterBlue: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_twitter_logo),
        contentDescription = "Twitter Logo",
        tint = twitterBlue,
        modifier = modifier.size(80.dp)
    )
}
