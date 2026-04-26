package com.twitter.twitterui.presentation.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TwitterPostButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    isLoading: Boolean,
    twitterBlue: Color,
    text: String,
    modifier: Modifier = Modifier
) {
    val isActuallyEnabled = isEnabled && !isLoading
    val textColor = if (isActuallyEnabled) Color.White else Color.DarkGray

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = twitterBlue,
            disabledContainerColor = Color.LightGray
        ),
        shape = RoundedCornerShape(12.dp),
        enabled = isActuallyEnabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
        } else {
            Text(
                text = text,
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
