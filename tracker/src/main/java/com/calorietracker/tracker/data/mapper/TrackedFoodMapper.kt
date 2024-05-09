package com.calorietracker.tracker.data.mapper

import com.calorietracker.tracker.data.local.entity.TrackedFoodEntity
import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackedFood
import java.time.LocalDate

fun TrackedFoodEntity.toTrackedFood(): TrackedFood {
    return TrackedFood(
        id = id,
        name = name,
        carbs = carbs,
        proteins = proteins,
        fats = fats,
        imageUrl = imageUrl,
        mealType = MealType.fromString(type),
        amount = amount,
        date = LocalDate.of(year, month, dayOfMonth),
        calories = calories,
    )
}

fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        id = id,
        name = name,
        carbs = carbs,
        proteins = proteins,
        fats = fats,
        imageUrl = imageUrl,
        type = mealType.name,
        amount = amount,
        dayOfMonth = date.dayOfMonth,
        month = date.monthValue,
        year = date.year,
        calories = calories,
    )
}