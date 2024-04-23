package com.calorietracker.android.utils.navigation

import androidx.navigation.NavController
import com.calorietracker.core.utils.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    navigate(event.route)
}