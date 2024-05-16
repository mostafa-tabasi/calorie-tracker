package com.calorietracker.tracker.presentation.search

import com.calorietracker.tracker.domain.model.TrackableFood

data class TrackableFoodUiState(
    val food: TrackableFood,
    val isExpanded: Boolean = false,
    val amount: String = "",
)
