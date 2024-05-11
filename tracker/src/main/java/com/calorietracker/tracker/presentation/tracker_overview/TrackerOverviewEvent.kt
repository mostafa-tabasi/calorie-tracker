package com.calorietracker.tracker.presentation.tracker_overview

import com.calorietracker.tracker.domain.model.TrackedFood

sealed class TrackerOverviewEvent {
    data object OnNextDayClick : TrackerOverviewEvent()
    data object OnPreviousDayClick : TrackerOverviewEvent()
    data class OnToggleMealClick(val meal: Meal) : TrackerOverviewEvent()
    data class OnDeleteTrackedFoodClick(val trackedFood: TrackedFood) : TrackerOverviewEvent()
    data class OnAddFoodClick(val meal: Meal) : TrackerOverviewEvent()
}
