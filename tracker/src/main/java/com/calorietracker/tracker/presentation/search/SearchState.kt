package com.calorietracker.tracker.presentation.search

data class SearchState(
    val query: String = "",
    val isSearching: Boolean = false,
    val foods: List<TrackableFoodUiState> = emptyList(),
)