package com.calorietracker.tracker.presentation.tracker_overview.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DaySelector(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onNextDayClick: () -> Unit,
    onPreviousDayClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onPreviousDayClick,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                contentDescription = stringResource(id = R.string.previous_day),
                tint = MaterialTheme.colors.onBackground,
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = dateFormatter(date),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
        )
        IconButton(
            onClick = onNextDayClick,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Sharp.ArrowForward,
                contentDescription = stringResource(id = R.string.next_day),
                tint = MaterialTheme.colors.onBackground,
            )
        }
    }
}

@Composable
private fun dateFormatter(date: LocalDate): String {
    val today = LocalDate.now()

    return when (date) {
        today -> stringResource(id = R.string.today)
        today.plusDays(1) -> stringResource(id = R.string.tomorrow)
        today.minusDays(1) -> stringResource(id = R.string.yesterday)
        else -> DateTimeFormatter.ofPattern("dd MMMM").format(date)
    }
}

@Preview(showBackground = true)
@Composable
private fun DaySelectorPreview() {
    CalorieTrackerTheme {
        DaySelector(
            date = LocalDate.now().minusDays(1),
            onPreviousDayClick = {},
            onNextDayClick = {},
        )
    }
}