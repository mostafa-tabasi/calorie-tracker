package com.calorietracker.tracker.presentation.tracker_overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.tracker.presentation.tracker_overview.component.NutrientsHeader

@Composable
fun TrackerOverviewScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {

    TrackerOverviewLayout(
        state = viewModel.state
    )
}

@Composable
fun TrackerOverviewLayout(
    state: TrackerOverviewState,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        NutrientsHeader(state = state)
        // Day Changer
        LazyColumn(Modifier.weight(1f)) {

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackerOverviewPreview() {
    CalorieTrackerTheme {
        TrackerOverviewLayout(
            state = TrackerOverviewState(
                totalCarbs = 50,
                totalProteins = 70,
                totalFats = 35,
                totalCalories = 450,
                carbsGoal = 70,
                proteinsGoal = 60,
                fatsGoal = 80,
                caloriesGoal = 940,
            ),
        )
    }
}