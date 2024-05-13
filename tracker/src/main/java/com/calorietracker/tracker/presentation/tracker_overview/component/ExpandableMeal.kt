package com.calorietracker.tracker.presentation.tracker_overview.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.core.utils.UiText
import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.presentation.component.NutrientInfo
import com.calorietracker.tracker.presentation.component.UnitDisplay
import com.calorietracker.tracker.presentation.tracker_overview.Meal

@Composable
fun ExpandableMeal(
    modifier: Modifier = Modifier,
    meal: Meal,
    onToggleClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .padding(spacing.small),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(meal.drawableRes),
                contentDescription = meal.name.asString(context),
                modifier = Modifier.weight(1f),
            )
            Column(
                modifier = Modifier
                    .weight(2.5f)
                    .padding(start = spacing.small),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = meal.name.asString(context),
                        color = MaterialTheme.colors.onBackground,
                    )
                    Icon(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(100))
                            .clickable { onToggleClick() }
                            .padding(spacing.extraSmall),
                        imageVector = if (meal.isExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (meal.isExpanded) stringResource(id = R.string.collapse)
                        else stringResource(id = R.string.extend),
                    )
                }
                Spacer(modifier = Modifier.height(spacing.small))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    UnitDisplay(
                        modifier = Modifier.weight(1f),
                        amount = meal.calories,
                        amountTextSize = 24.sp,
                        unit = stringResource(id = R.string.kcal),
                    )
                    Spacer(modifier = Modifier.width(spacing.small))
                    NutrientInfo(
                        name = stringResource(id = R.string.carbs),
                        nameTextSize = 12.sp,
                        amount = meal.carbs,
                        unit = stringResource(id = R.string.grams),
                    )
                    Spacer(modifier = Modifier.width(spacing.small))
                    NutrientInfo(
                        name = stringResource(id = R.string.protein),
                        nameTextSize = 12.sp,
                        amount = meal.proteins,
                        unit = stringResource(id = R.string.grams),
                    )
                    Spacer(modifier = Modifier.width(spacing.small))
                    NutrientInfo(
                        name = stringResource(id = R.string.fat),
                        nameTextSize = 12.sp,
                        amount = meal.fats,
                        unit = stringResource(id = R.string.grams),
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = meal.isExpanded,
            enter = slideInVertically()
                    + expandVertically(expandFrom = Alignment.Top)
                    + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically()
                    + shrinkVertically()
                    + fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)),
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandableMealPreview() {
    CalorieTrackerTheme {
        ExpandableMeal(
            meal = Meal(
                name = UiText.StringResource(R.string.breakfast),
                drawableRes = R.drawable.ic_breakfast,
                mealType = MealType.Breakfast,
                carbs = 42,
                proteins = 119,
                fats = 21,
                calories = 372,
                isExpanded = false,
            ),
            onToggleClick = {}) {
            Text(
                text = "HELLO WORLD",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}