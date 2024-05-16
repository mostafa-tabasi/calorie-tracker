package com.calorietracker.tracker.presentation.tracker_overview.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackedFood
import com.calorietracker.tracker.presentation.component.NutrientInfo
import com.calorietracker.tracker.presentation.component.UnitDisplay
import java.time.LocalDate

@Composable
fun TrackedFoodItem(
    modifier: Modifier = Modifier,
    food: TrackedFood,
    onDeleteClick: () -> Unit,
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .padding(spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(food.imageUrl)
                .crossfade(true)
                .fallback(R.drawable.ic_burger)
                .error(R.drawable.ic_burger)
                .build(),
            contentDescription = food.name,
            placeholder = painterResource(R.drawable.ic_burger),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(100.dp)
                .aspectRatio(1f),
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
                    text = food.name,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h4,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Icon(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(100))
                        .clickable { onDeleteClick() }
                        .padding(spacing.extraSmall),
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.delete),
                )
            }
            Spacer(modifier = Modifier.height(spacing.small))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    UnitDisplay(
                        amount = food.amount,
                        amountTextSize = 12.sp,
                        unit = stringResource(id = R.string.grams),
                    )
                    UnitDisplay(
                        amount = food.calories,
                        amountTextSize = 12.sp,
                        unit = stringResource(id = R.string.kcal),
                    )
                }
                Spacer(modifier = Modifier.width(spacing.small))
                NutrientInfo(
                    name = stringResource(id = R.string.carbs),
                    nameTextSize = 12.sp,
                    amount = food.carbs,
                    unit = stringResource(id = R.string.grams),
                )
                Spacer(modifier = Modifier.width(spacing.small))
                NutrientInfo(
                    name = stringResource(id = R.string.protein),
                    nameTextSize = 12.sp,
                    amount = food.proteins,
                    unit = stringResource(id = R.string.grams),
                )
                Spacer(modifier = Modifier.width(spacing.small))
                NutrientInfo(
                    name = stringResource(id = R.string.fat),
                    nameTextSize = 12.sp,
                    amount = food.fats,
                    unit = stringResource(id = R.string.grams),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackedFoodPreview() {
    CalorieTrackerTheme {
        TrackedFoodItem(
            food = TrackedFood(
                id = 123,
                name = "Sandwich",
                imageUrl = null,
                carbs = 23,
                proteins = 12,
                fats = 20,
                calories = 135,
                mealType = MealType.Dinner,
                amount = 230,
                date = LocalDate.now(),
            ),
            onDeleteClick = {},
        )
    }
}