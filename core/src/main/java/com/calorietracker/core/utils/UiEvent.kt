package com.calorietracker.core.utils

import com.calorietracker.core.utils.navigation.Route

sealed class UiEvent {
    data class Navigate(val route: Route) : UiEvent()
    data object NavigateUp : UiEvent()
    data class ShowSnackbar(val message: UiText) : UiEvent()
}