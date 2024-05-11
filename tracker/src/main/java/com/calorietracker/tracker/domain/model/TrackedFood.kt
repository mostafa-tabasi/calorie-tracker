package com.calorietracker.tracker.domain.model

import java.time.LocalDate

data class TrackedFood(
    val name: String,
    val imageUrl: String?,
    val carbs: Int,
    val proteins: Int,
    val fats: Int,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDate,
    val calories: Int,
    val id: Int? = null,
)
