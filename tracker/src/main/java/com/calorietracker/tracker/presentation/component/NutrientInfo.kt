package com.calorietracker.tracker.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.calorietracker.core.ui.theme.CalorieTrackerTheme

@Composable
fun NutrientInfo(
    modifier: Modifier = Modifier,
    name: String,
    nameTextSize: TextUnit = 17.sp,
    amount: Int,
    amountTextSize: TextUnit = 17.sp,
    unit: String,
    unitTextSize: TextUnit = 13.sp,
    textColor: Color = MaterialTheme.colors.onBackground,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UnitDisplay(
            amount = amount,
            amountTextSize = amountTextSize,
            unit = unit,
            unitTextSize = unitTextSize,
            textColor = textColor,
        )
        Text(
            text = name,
            color = textColor,
            fontSize = nameTextSize,
            fontWeight = FontWeight.Light,
        )
    }
}

@Preview
@Composable
private fun NutrientInfoPreview() {
    CalorieTrackerTheme {
        NutrientInfo(
            name = "Protein",
            amount = 177,
            unit = "kcal",
            textColor = MaterialTheme.colors.onPrimary,
        )
    }
}