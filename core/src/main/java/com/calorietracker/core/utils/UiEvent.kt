package com.calorietracker.core.utils

sealed class UiEvent {
    data object NavigateToNextScreen : UiEvent()
    data object NavigateUp : UiEvent()
    data class ShowSnackbar(val message: UiText) : UiEvent()
}