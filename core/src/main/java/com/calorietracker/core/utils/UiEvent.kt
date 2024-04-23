package com.calorietracker.core.utils

sealed interface UiEvent {
    data class Navigate(val route: String) : UiEvent
    data object NavigateUp : UiEvent
}