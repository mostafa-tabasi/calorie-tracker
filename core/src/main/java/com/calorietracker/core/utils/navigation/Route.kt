package com.calorietracker.core.utils.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object Welcome : Route()

    @Serializable
    data object Age : Route()

    @Serializable
    data object Gender : Route()

    @Serializable
    data object Height : Route()

    @Serializable
    data object Weight : Route()

    @Serializable
    data object NutrientGoal : Route()

    @Serializable
    data object Activity : Route()

    @Serializable
    data object Goal : Route()

    @Serializable
    data object TrackerOverview : Route()

    @Serializable
    data class Search(
        val mealName: String,
        val dayOfMonth: Int,
        val month: Int,
        val year: Int,
    ) : Route()
}