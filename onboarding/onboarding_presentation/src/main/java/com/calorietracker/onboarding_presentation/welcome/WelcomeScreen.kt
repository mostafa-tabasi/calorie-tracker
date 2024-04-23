package com.calorietracker.onboarding_presentation.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.calorietracker.core.R
import com.calorietracker.core_ui.theme.CalorieTrackerTheme
import com.calorietracker.core_ui.theme.LocalSpacing
import com.calorietracker.onboarding_presentation.components.ActionButton

@Composable
fun WelcomeScreen() {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.welcome_text),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(spacing.medium))
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = {},
            isEnabled = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    CalorieTrackerTheme {
        WelcomeScreen()
    }
}