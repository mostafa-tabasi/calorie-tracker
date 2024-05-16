package com.calorietracker.tracker.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.R
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.UiText
import com.calorietracker.tracker.domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutNumber: FilterOutNumber,
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var searchJob: Job? = null

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> state = state.copy(query = event.query)

            is SearchEvent.OnSearchClick -> executeSearch()

            is SearchEvent.OnToggleTrackableFood -> {
                state = state.copy(
                    foods = state.foods.map {
                        if (it.food == event.food) it.copy(isExpanded = !it.isExpanded)
                        else it
                    }
                )
            }

            is SearchEvent.OnFoodAmountChange -> {
                state = state.copy(
                    foods = state.foods.map {
                        if (it.food == event.food) it.copy(
                            amount = filterOutNumber(event.amount, maxLength = 3)
                        )
                        else it
                    }
                )
            }

            is SearchEvent.OnTrackFoodClick -> trackFood(event)
        }
    }

    private fun executeSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            state = state.copy(
                isSearching = true,
                foods = emptyList(),
            )
            trackerUseCases
                .searchFood(state.query)
                .onSuccess { foods ->
                    state = state.copy(
                        isSearching = false,
                        query = "",
                        foods = foods.map { TrackableFoodUiState(it) },
                    )
                }
                .onFailure {
                    state = state.copy(isSearching = false)
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.error_something_went_wrong)
                        )
                    )
                }
        }
    }

    private fun trackFood(
        event: SearchEvent.OnTrackFoodClick,
    ) {
        viewModelScope.launch {
            trackerUseCases.trackFood(
                food = event.trackableFood.food,
                amount = event.trackableFood.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date,
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}