package com.calorietracker.tracker.domain.use_case

import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackableFood
import com.calorietracker.tracker.domain.model.TrackedFood
import com.calorietracker.tracker.domain.repository.TrackerRepository
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFood(
    private val repository: TrackerRepository,
) {
    suspend operator fun invoke(
        food: TrackableFood,
        amount: Int,
        mealType: MealType,
        date: LocalDate,
    ) {
        repository.insertTrackedFood(
            TrackedFood(
                name = food.name,
                imageUrl = food.imageUrl,
                carbs = ((food.carbsPer100g / 100f) * amount).roundToInt(),
                proteins = ((food.proteinsPer100g / 100f) * amount).roundToInt(),
                fats = ((food.fatsPer100g / 100f) * amount).roundToInt(),
                calories = ((food.caloriesPer100g / 100f) * amount).roundToInt(),
                mealType = mealType,
                amount = amount,
                date = date,
            )
        )
    }
}