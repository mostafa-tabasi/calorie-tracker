package com.calorietracker.tracker.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackableFood
import com.calorietracker.tracker.presentation.search.component.ExpandableTrackableFood
import com.calorietracker.tracker.presentation.search.component.SearchTextField
import java.time.LocalDate
import java.util.Locale

@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    onNavigateUp: () -> Boolean,
    viewModel: SearchViewModel = hiltViewModel(),
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.NavigateUp -> onNavigateUp()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.message.asString(context),
                    )
                }

                else -> Unit
            }
        }
    }

    SearchLayout(
        state = viewModel.state,
        mealName = mealName,
        onTextChange = { viewModel.onEvent(SearchEvent.OnQueryChange(it)) },
        onSearchClick = { viewModel.onEvent(SearchEvent.OnSearchClick) },
        onToggleTrackableFood = { viewModel.onEvent(SearchEvent.OnToggleTrackableFood(it)) },
        onFoodAmountChange = { food, amount ->
            viewModel.onEvent(
                SearchEvent.OnFoodAmountChange(food, amount)
            )
        },
        onTrackFoodClick = {
            viewModel.onEvent(
                SearchEvent.OnTrackFoodClick(
                    trackableFood = it,
                    mealType = MealType.fromString(mealName),
                    date = LocalDate.of(year, month, dayOfMonth)
                )
            )
        },
    )
}

@Composable
private fun SearchLayout(
    state: SearchState,
    mealName: String,
    onTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onToggleTrackableFood: (TrackableFood) -> Unit,
    onFoodAmountChange: (TrackableFood, String) -> Unit,
    onTrackFoodClick: (TrackableFoodUiState) -> Unit,
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.small),
    ) {
        SearchTextField(
            hint = stringResource(
                id = R.string.add_meal, mealName.replaceFirstChar { it.titlecase(Locale.ROOT) }
            ),
            text = state.query,
            onTextChange = onTextChange,
            onSearchClick = onSearchClick,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            when {
                state.isSearching -> CircularProgressIndicator()
                state.foods.isEmpty() -> Text(
                    text = stringResource(id = R.string.no_results),
                    style = MaterialTheme.typography.h2,
                )

                else -> LazyColumn {
                    items(state.foods) {
                        ExpandableTrackableFood(
                            foodState = it,
                            onToggleChange = { onToggleTrackableFood(it.food) },
                            onAmountChange = { amount -> onFoodAmountChange(it.food, amount) },
                            onTrackClick = { onTrackFoodClick(it) }
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun SearchLayoutPreview() {
    CalorieTrackerTheme {
        SearchLayout(
            state = SearchState(
                "",
                isSearching = true,
            ),
            mealName = MealType.Dinner.name,
            onTextChange = {},
            onSearchClick = {},
            onToggleTrackableFood = {},
            onFoodAmountChange = { _, _ -> },
            onTrackFoodClick = {},
        )
    }
}