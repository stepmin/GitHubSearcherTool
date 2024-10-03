package com.example.www.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(text)
    }
}

@Preview
@Composable
private fun InfoBoxPreview()  {
    InfoBox(text = "No data available")
}