package com.calorietracker.android.data.repository

import com.calorietracker.tracker.domain.model.TrackableFood
import com.calorietracker.tracker.domain.model.TrackedFood
import com.calorietracker.tracker.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate
import kotlin.random.Random

class TrackerRepositoryFake : TrackerRepository {

    var shouldReturnError = false
    var searchResult = listOf<TrackableFood>()
    private val trackedFood = mutableListOf<TrackedFood>()
    private val foodsForDateFlow =
        MutableSharedFlow<List<TrackedFood>>(replay = 1)

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return if (shouldReturnError) Result.failure(Throwable())
        else Result.success(searchResult)
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        trackedFood.add(food.copy(id = Random.nextInt()))
        foodsForDateFlow.emit(trackedFood)
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        trackedFood.remove(food)
        foodsForDateFlow.emit(trackedFood)
    }

    override fun getFoodsForDate(date: LocalDate): Flow<List<TrackedFood>> {
        return foodsForDateFlow
    }
}