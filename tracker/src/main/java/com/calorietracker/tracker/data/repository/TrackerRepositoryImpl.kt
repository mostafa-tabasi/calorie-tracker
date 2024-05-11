package com.calorietracker.tracker.data.repository

import com.calorietracker.tracker.data.local.TrackerDao
import com.calorietracker.tracker.data.mapper.toTrackableFood
import com.calorietracker.tracker.data.mapper.toTrackedFood
import com.calorietracker.tracker.data.mapper.toTrackedFoodEntity
import com.calorietracker.tracker.data.remote.OpenFoodApi
import com.calorietracker.tracker.domain.model.TrackableFood
import com.calorietracker.tracker.domain.model.TrackedFood
import com.calorietracker.tracker.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TrackerRepositoryImpl @Inject constructor(
    private val dao: TrackerDao,
    private val api: OpenFoodApi,
) : TrackerRepository {

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val response = api.searchFood(
                query,
                page,
                pageSize,
            )
            if (response.isSuccessful) Result.success(
                response.body()?.products?.mapNotNull { it.toTrackableFood() }
                    ?: arrayListOf()
            )
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(date: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = date.dayOfMonth,
            month = date.monthValue,
            year = date.year,
        ).map { entities ->
            entities.map {
                it.toTrackedFood()
            }
        }
    }
}