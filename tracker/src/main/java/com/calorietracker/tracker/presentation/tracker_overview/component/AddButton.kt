package com.calorietracker.tracker.presentation.tracker_overview.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.primary,
    borderWidth: Dp = LocalSpacing.current.xxsSmall
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(LocalSpacing.current.large),
        border = BorderStroke(
            width = borderWidth,
            color = color,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add),
                tint = color,
            )
            Text(
                text = text,
                modifier = Modifier.padding(LocalSpacing.current.small),
                color = color,
                style = MaterialTheme.typography.button,
            )
        }
    }
}

/*
stringResource(
    id = R.string.add_meal,
    meal.name.replaceFirstChar { it.titlecase(Locale.ROOT) },
)
*/

@Preview
@Composable
private fun AddFoodButtonPreview() {
    CalorieTrackerTheme {
        AddButton(text = "Add Breakfast", onClick = {})
    }
}