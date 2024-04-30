package com.calorietracker.onboarding_presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.layoutId
import com.calorietracker.core_ui.theme.LocalSpacing

@Composable
fun DescriptionText(
    id: String = "",
    description: String,
) {
    Text(
        text = description,
        style = MaterialTheme.typography.h1,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .layoutId(id)
            .fillMaxWidth()
            .padding(bottom = LocalSpacing.current.medium)
    )
}