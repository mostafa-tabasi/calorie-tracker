package com.calorietracker.onboarding_presentation.nutrient_goal

sealed class NutrientGoalEvent {
    data class OnCarbsRatioChange(val ratio: String) : NutrientGoalEvent()
    data class OnProteinsRatioChange(val ratio: String) : NutrientGoalEvent()
    data class OnFatsRatioChange(val ratio: String) : NutrientGoalEvent()
    data object OnNextClick : NutrientGoalEvent()
}
