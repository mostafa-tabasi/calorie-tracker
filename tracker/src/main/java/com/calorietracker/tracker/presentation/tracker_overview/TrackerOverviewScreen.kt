package com.calorietracker.tracker.presentation.tracker_overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.tracker.presentation.tracker_overview.component.DaySelector
import com.calorietracker.tracker.presentation.tracker_overview.component.ExpandableMeal
import com.calorietracker.tracker.presentation.tracker_overview.component.NutrientsHeader

@Composable
fun TrackerOverviewScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {

    TrackerOverviewLayout(
        state = viewModel.state,
        onNextDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick) },
        onPreviousDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick) },
        onMealToggleClick = { viewModel.onEvent(TrackerOverviewEvent.OnToggleMealClick(it)) }
    )
}

@Composable
fun TrackerOverviewLayout(
    state: TrackerOverviewState,
    onNextDayClick: () -> Unit,
    onPreviousDayClick: () -> Unit,
    onMealToggleClick: (Meal) -> Unit,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        NutrientsHeader(state = state)
        DaySelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.small),
            date = state.date,
            onPreviousDayClick = onPreviousDayClick,
            onNextDayClick = onNextDayClick,
        )
        LazyColumn(Modifier.weight(1f)) {
            items(state.meals) {
                ExpandableMeal(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = spacing.small,
                            vertical = spacing.extraSmall,
                        ),
                    meal = it,
                    onToggleClick = { onMealToggleClick(it) },
                ) {

                }
            }
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
            onNextDayClick = {},
            onPreviousDayClick = {},
            onMealToggleClick = {},
        )
    }
}