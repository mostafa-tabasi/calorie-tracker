package com.calorietracker.tracker.presentation.tracker_overview.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.CarbColor
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.tracker.presentation.component.NutrientInfo

@Composable
fun NutrientCircleProgress(
    modifier: Modifier = Modifier,
    value: Int,
    goal: Int,
    name: String,
    unit: String,
    color: Color,
    strokeWidth: Dp = LocalSpacing.current.small,
) {
    val valueExceededColor = MaterialTheme.colors.error

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(strokeWidth / 2),
    ) {
        NutrientInfo(
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalSpacing.current.small),
            name = name,
            amount = value,
            unit = unit,
            textColor = if (value > goal) valueExceededColor
            else MaterialTheme.colors.onPrimary,
        )

        if (value > goal) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                fullCircle(
                    color = valueExceededColor,
                    size = size,
                    strokeWidth = strokeWidth,
                )
            }
        } else {
            val backgroundColor = MaterialTheme.colors.background

            val progressAmount = remember { Animatable(0f) }
            LaunchedEffect(key1 = value) {
                progressAmount.animateTo(
                    targetValue = if (goal > 0) value / goal.toFloat() else 0f,
                    animationSpec = tween(durationMillis = 300),
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                fullCircle(
                    color = backgroundColor,
                    size = size,
                    strokeWidth = strokeWidth,
                )
                drawArc(
                    color = color,
                    startAngle = 90f,
                    sweepAngle = (360f * progressAmount.value),
                    useCenter = false,
                    size = size,
                    style = Stroke(width = strokeWidth.toPx())
                )
            }
        }
    }

}

private fun DrawScope.fullCircle(
    color: Color,
    size: Size,
    strokeWidth: Dp,
) {
    drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        size = size,
        style = Stroke(width = strokeWidth.toPx())
    )
}

@Preview
@Composable
private fun NutrientCircleProgressPreview() {
    CalorieTrackerTheme {
        NutrientCircleProgress(
            value = 25,
            goal = 100,
            name = "Carbs",
            unit = "kcal",
            color = CarbColor,
        )
    }
}