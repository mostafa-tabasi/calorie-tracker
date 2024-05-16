package com.calorietracker.tracker.presentation.search

import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackableFood
import java.time.LocalDate

sealed class SearchEvent {
    data class OnQueryChange(val query: String) : SearchEvent()
    data object OnSearchClick : SearchEvent()
    data class OnToggleTrackableFood(val food: TrackableFood) : SearchEvent()
    data class OnFoodAmountChange(
        val food: TrackableFood,
        val amount: String,
    ) : SearchEvent()

    data class OnTrackFoodClick(
        val trackableFood: TrackableFoodUiState,
        val mealType: MealType,
        val date: LocalDate,
    ) : SearchEvent()
}