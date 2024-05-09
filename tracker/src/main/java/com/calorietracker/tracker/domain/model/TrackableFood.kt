package com.calorietracker.tracker.domain.model

data class TrackableFood(
    val name: String,
    val imageUrl: String?,
    val caloriesPer100g: Int,
    val carbsPer100g: Int,
    val proteinsPer100g: Int,
    val fatsPer100g: Int,
)
