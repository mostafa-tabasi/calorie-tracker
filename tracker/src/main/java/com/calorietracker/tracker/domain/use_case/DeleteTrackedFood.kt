package com.calorietracker.tracker.domain.use_case

import com.calorietracker.tracker.domain.model.TrackedFood
import com.calorietracker.tracker.domain.repository.TrackerRepository

class DeleteTrackedFood(
    private val repository: TrackerRepository,
) {
    suspend operator fun invoke(
        food: TrackedFood,
    ) {
        repository.deleteTrackedFood(food)
    }
}