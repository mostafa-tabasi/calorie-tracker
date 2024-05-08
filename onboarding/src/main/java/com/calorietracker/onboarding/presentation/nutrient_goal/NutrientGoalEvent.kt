package com.calorietracker.onboarding.presentation.nutrient_goal

sealed class NutrientGoalEvent {
    data class OnCarbsRatioChange(val ratio: String) : NutrientGoalEvent()
    data class OnProteinsRatioChange(val ratio: String) : NutrientGoalEvent()
    data class OnFatsRatioChange(val ratio: String) : NutrientGoalEvent()
    data object OnNextClick : NutrientGoalEvent()
}
