package com.calorietracker.tracker.presentation.tracker_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackedFood
import com.calorietracker.tracker.presentation.tracker_overview.component.AddButton
import com.calorietracker.tracker.presentation.tracker_overview.component.DaySelector
import com.calorietracker.tracker.presentation.tracker_overview.component.ExpandableMeal
import com.calorietracker.tracker.presentation.tracker_overview.component.NutrientsHeader
import com.calorietracker.tracker.presentation.tracker_overview.component.TrackedFoodItem
import java.time.LocalDate
import java.util.Locale

@Composable
fun TrackerOverviewScreen(
    innerPadding: PaddingValues,
    onNavigateToSearch: (mealName: String, dayOfMonth: Int, month: Int, year: Int) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    TrackerOverviewLayout(
        innerPadding = innerPadding,
        state = viewModel.state,
        onNextDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick) },
        onPreviousDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick) },
        onMealToggleClick = { viewModel.onEvent(TrackerOverviewEvent.OnToggleMealClick(it)) },
        onTrackedFoodDeleteClick = {
            viewModel.onEvent(TrackerOverviewEvent.OnDeleteTrackedFoodClick(it))
        },
        onAddFoodClick = { mealName, dayOfMonth, month, year ->
            onNavigateToSearch(
                mealName,
                dayOfMonth,
                month,
                year,
            )
        },
    )
}

@Composable
fun TrackerOverviewLayout(
    innerPadding: PaddingValues,
    state: TrackerOverviewState,
    onNextDayClick: () -> Unit,
    onPreviousDayClick: () -> Unit,
    onMealToggleClick: (Meal) -> Unit,
    onTrackedFoodDeleteClick: (TrackedFood) -> Unit,
    onAddFoodClick: (mealName: String, dayOfMonth: Int, month: Int, year: Int) -> Unit,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        NutrientsHeader(
            innerPadding = innerPadding,
            state = state,
        )
        DaySelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.small),
            date = state.date,
            onPreviousDayClick = onPreviousDayClick,
            onNextDayClick = onNextDayClick,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(state.meals) { meal ->
                ExpandableMeal(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = spacing.small,
                            vertical = spacing.extraSmall,
                        ),
                    meal = meal,
                    toggleButtonTestTag = "trackerOverview:toggleButton:${meal.mealType.name}",
                    onToggleClick = { onMealToggleClick(meal) },
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(
                                    top = spacing.small,
                                    start = spacing.small,
                                    end = spacing.small,
                                )
                                .background(
                                    shape = RoundedCornerShape(spacing.small),
                                    color = MaterialTheme.colors.background,
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val trackedFood = state.trackedFoods.filter {
                                it.mealType == meal.mealType
                            }
                            trackedFood.forEachIndexed { i, food ->
                                TrackedFoodItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = spacing.small),
                                    food = food,
                                    onDeleteClick = { onTrackedFoodDeleteClick(food) },
                                )
                                if (i < trackedFood.lastIndex)
                                    Divider(modifier = Modifier.padding(horizontal = spacing.medium))
                            }
                            AddButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = spacing.medium,
                                        vertical = spacing.small,
                                    )
                                    .testTag("trackerOverview:addButton:${meal.mealType.name}"),
                                text = stringResource(
                                    id = R.string.add_meal,
                                    meal.name.asString(context)
                                        .replaceFirstChar { it.titlecase(Locale.ROOT) },
                                ),
                                onClick = {
                                    onAddFoodClick(
                                        meal.mealType.name,
                                        state.date.dayOfMonth,
                                        state.date.monthValue,
                                        state.date.year,
                                    )
                                },
                            )
                        }
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackerOverviewPreview() {
    CalorieTrackerTheme {
        TrackerOverviewLayout(
            innerPadding = PaddingValues(0.dp),
            state = TrackerOverviewState(
                totalCarbs = 50,
                totalProteins = 70,
                totalFats = 35,
                totalCalories = 450,
                carbsGoal = 70,
                proteinsGoal = 60,
                fatsGoal = 80,
                caloriesGoal = 940,
                trackedFoods = arrayListOf(
                    TrackedFood(
                        id = 123,
                        name = "Sandwich",
                        imageUrl = null,
                        carbs = 23,
                        proteins = 12,
                        fats = 20,
                        calories = 135,
                        mealType = MealType.Lunch,
                        amount = 230,
                        date = LocalDate.now(),
                    ),
                    TrackedFood(
                        id = 123,
                        name = "Sandwich",
                        imageUrl = null,
                        carbs = 23,
                        proteins = 12,
                        fats = 20,
                        calories = 135,
                        mealType = MealType.Lunch,
                        amount = 230,
                        date = LocalDate.now(),
                    ),
                ),
                meals = defaultMeals.map {
                    if (it.mealType == MealType.Lunch)
                        it.copy(isExpanded = true)
                    else it
                }
            ),
            onNextDayClick = {},
            onPreviousDayClick = {},
            onMealToggleClick = {},
            onTrackedFoodDeleteClick = {},
            onAddFoodClick = { _, _, _, _ -> },
        )
    }
}