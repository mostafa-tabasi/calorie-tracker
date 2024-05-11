package com.calorietracker.tracker.presentation.tracker_overview.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.CarbColor
import com.calorietracker.core.ui.theme.FatColor
import com.calorietracker.core.ui.theme.ProteinColor

@Composable
fun NutrientsBar(
    modifier: Modifier = Modifier,
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    calorieGoal: Int,
) {
    val radiusValue = 100f

    if (calories > calorieGoal) {
        val caloriesExceedColor = MaterialTheme.colors.error

        Canvas(modifier = modifier) {
            drawRoundRect(
                color = caloriesExceedColor,
                size = size,
                cornerRadius = CornerRadius(radiusValue)
            )
        }
    } else {
        val backgroundColor = MaterialTheme.colors.background

        val carbsWidthRatio = remember { Animatable(0f) }
        val proteinWidthRatio = remember { Animatable(0f) }
        val fatWidthRatio = remember { Animatable(0f) }

        LaunchedEffect(key1 = carbs) {
            //                                              Carbohydrates provide 4 calories per gram
            carbsWidthRatio.animateTo(targetValue = (carbs * 4f) / calorieGoal)
        }
        LaunchedEffect(key1 = protein) {
            //                                                  Protein provide 4 calories per gram
            proteinWidthRatio.animateTo(targetValue = (protein * 4f) / calorieGoal)
        }
        LaunchedEffect(key1 = fat) {
            //                                          Fat provide 9 calories per gram
            fatWidthRatio.animateTo(targetValue = (fat * 9f) / calorieGoal)
        }

        Canvas(modifier = modifier) {
            val carbsWidth = carbsWidthRatio.value * size.width
            val proteinWidth = proteinWidthRatio.value * size.width
            val fatWidth = fatWidthRatio.value * size.width

            drawRoundRect(
                color = backgroundColor,
                size = size,
                cornerRadius = CornerRadius(radiusValue),
            )
            drawRoundRect(
                color = FatColor,
                size = Size(
                    width = carbsWidth + proteinWidth + fatWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(radiusValue),
            )
            drawRoundRect(
                color = ProteinColor,
                size = Size(
                    width = carbsWidth + proteinWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(radiusValue),
            )
            drawRoundRect(
                color = CarbColor,
                size = Size(
                    width = carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(radiusValue),
            )
        }
    }
}

@Preview
@Composable
private fun NutrientsBarPreview() {
    CalorieTrackerTheme {
        NutrientsBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            carbs = 2,
            protein = 5,
            fat = 1,
            calories = 66,
            calorieGoal = 100,
        )
    }
}