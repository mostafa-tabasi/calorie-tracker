package com.calorietracker.tracker.presentation.tracker_overview.component

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.CarbColor
import com.calorietracker.core.ui.theme.FatColor
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.core.ui.theme.ProteinColor
import com.calorietracker.tracker.presentation.component.UnitDisplay
import com.calorietracker.tracker.presentation.tracker_overview.TrackerOverviewState

@Composable
fun NutrientsHeader(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    state: TrackerOverviewState,
) {
    val spacing = LocalSpacing.current
    val colors = MaterialTheme.colors

    val animatedCaloriesCount = animateIntAsState(
        targetValue = state.totalCalories,
        label = "animatedCalories",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(
                RoundedCornerShape(
                    bottomStart = spacing.large,
                    bottomEnd = spacing.large,
                )
            )
            .background(color = MaterialTheme.colors.primary)
            .padding(
                start = spacing.medium,
                end = spacing.medium,
                top = innerPadding.calculateTopPadding() + spacing.small,
                bottom = spacing.small,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            UnitDisplay(
                modifier = Modifier.alignBy(LastBaseline),
                amount = animatedCaloriesCount.value,
                amountTextSize = 30.sp,
                unit = stringResource(id = R.string.kcal),
                textColor = colors.onPrimary,
            )
            Column(modifier = Modifier.alignBy(LastBaseline)) {
                Text(
                    text = stringResource(id = R.string.your_goal),
                    color = colors.onPrimary,
                    fontSize = 13.sp,
                )
                UnitDisplay(
                    amount = state.caloriesGoal,
                    amountTextSize = 30.sp,
                    unit = stringResource(id = R.string.kcal),
                    textColor = colors.onPrimary,
                )
            }
        }
        Spacer(modifier = Modifier.height(spacing.small))
        NutrientsBarProgress(
            modifier = Modifier
                .fillMaxWidth()
                .height(spacing.large),
            carbs = state.totalCarbs,
            protein = state.totalProteins,
            fat = state.totalFats,
            calories = state.totalCalories,
            calorieGoal = state.caloriesGoal,
        )
        Spacer(modifier = Modifier.height(spacing.small))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacing.extraSmall),
        ) {
            NutrientCircleProgress(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = spacing.medium),
                value = state.totalCarbs,
                goal = state.carbsGoal,
                name = stringResource(id = R.string.carbs),
                unit = stringResource(id = R.string.kcal),
                color = CarbColor
            )
            NutrientCircleProgress(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = spacing.medium),
                value = state.totalProteins,
                goal = state.proteinsGoal,
                name = stringResource(id = R.string.protein),
                unit = stringResource(id = R.string.kcal),
                color = ProteinColor
            )
            NutrientCircleProgress(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = spacing.medium),
                value = state.totalFats,
                goal = state.fatsGoal,
                name = stringResource(id = R.string.fat),
                unit = stringResource(id = R.string.kcal),
                color = FatColor
            )
        }
    }
}

@Preview
@Composable
private fun NutrientsHeaderPreview() {
    CalorieTrackerTheme {
        NutrientsHeader(
            innerPadding = PaddingValues(0.dp),
            modifier = Modifier,
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