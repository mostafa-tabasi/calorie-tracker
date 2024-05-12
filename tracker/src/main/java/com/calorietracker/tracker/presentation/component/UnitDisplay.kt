package com.calorietracker.tracker.presentation.component

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing

@Composable
fun UnitDisplay(
    modifier: Modifier = Modifier,
    amount: Int,
    unit: String,
    amountTextSize: TextUnit = 17.sp,
    unitTextSize: TextUnit = 13.sp,
    textColor: Color = MaterialTheme.colors.onBackground,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min),
    ) {
        Text(
            text = amount.toString(),
            style = MaterialTheme.typography.h1,
            fontSize = amountTextSize,
            color = textColor,
            modifier = Modifier.alignBy(LastBaseline),
        )
        Spacer(modifier = Modifier.width(spacing.extraSmall))
        Text(
            text = unit,
            style = MaterialTheme.typography.h1,
            fontSize = unitTextSize,
            color = textColor,
            modifier = Modifier.alignBy(LastBaseline),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UnitDisplayPreview() {
    CalorieTrackerTheme {
        UnitDisplay(
            unit = "kcal",
            amount = 17,
        )
    }
}