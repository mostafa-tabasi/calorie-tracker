package com.calorietracker.tracker.domain.use_case

import com.calorietracker.tracker.domain.model.TrackedFood
import com.calorietracker.tracker.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDate(
    private val repository: TrackerRepository,
) {
    operator fun invoke(
        date: LocalDate,
    ): Flow<List<TrackedFood>> {
        return repository.getFoodsForDate(date)
    }
}