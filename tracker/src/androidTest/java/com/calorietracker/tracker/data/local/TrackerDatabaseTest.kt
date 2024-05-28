package com.calorietracker.tracker.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.calorietracker.tracker.data.local.entity.TrackedFoodEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackerDatabaseTest {

    private lateinit var database: TrackerDatabase
    private lateinit var dao: TrackerDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TrackerDatabase::class.java,
        ).build()
        dao = database.dao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTrackedFood() = runBlocking {
        // Given
        val day = 9
        val month = 10
        val year = 1991
        val trackedFood = TrackedFoodEntity(
            id = 1,
            name = "food",
            carbs = 123,
            proteins = 123,
            fats = 123,
            calories = 123,
            type = "type",
            imageUrl = "test.com/1.png",
            amount = 123,
            dayOfMonth = day,
            month = month,
            year = year,
        )

        // When
        dao.insertTrackedFood(trackedFood)

        // Then
        val foods = dao.getFoodsForDate(day, month, year).first()
        assertThat(foods).contains(trackedFood)
    }

    @Test
    fun overwriteTrackedFood() = runBlocking {
        // Given
        val day = 9
        val month = 10
        val year = 1991
        val trackedFood = TrackedFoodEntity(
            id = 1,
            name = "food",
            carbs = 123,
            proteins = 123,
            fats = 123,
            calories = 123,
            type = "type",
            imageUrl = "test.com/1.png",
            amount = 123,
            dayOfMonth = day,
            month = month,
            year = year,
        )
        dao.insertTrackedFood(trackedFood)

        // When
        val overwrittenFoodName = "overwritten food"
        val updatedFood = trackedFood.copy(name = overwrittenFoodName)
        dao.insertTrackedFood(updatedFood)

        // Then
        val foods = dao.getFoodsForDate(day, month, year).first()
        assertThat(foods).doesNotContain(trackedFood)
        assertThat(foods).contains(updatedFood)
    }

    @Test
    fun deleteTrackedFood() = runBlocking {
        // Given
        val day = 9
        val month = 10
        val year = 1991
        val trackedFood = TrackedFoodEntity(
            id = 1,
            name = "food",
            carbs = 123,
            proteins = 123,
            fats = 123,
            calories = 123,
            type = "type",
            imageUrl = "test.com/1.png",
            amount = 123,
            dayOfMonth = day,
            month = month,
            year = year,
        )
        dao.insertTrackedFood(trackedFood)

        // When
        dao.deleteTrackedFood(trackedFood)

        // Then
        val foods = dao.getFoodsForDate(day, month, year).first()
        assertThat(foods).doesNotContain(trackedFood)
    }

    @Test
    fun getTrackedFoodsForDifferentDate_mustReturnEmpty() = runBlocking {
        // Given
        val day = 9
        val month = 10
        val year = 1991
        val trackedFood = TrackedFoodEntity(
            id = 1,
            name = "food",
            carbs = 123,
            proteins = 123,
            fats = 123,
            calories = 123,
            type = "type",
            imageUrl = "test.com/1.png",
            amount = 123,
            dayOfMonth = day,
            month = month,
            year = year,
        )
        dao.insertTrackedFood(trackedFood)

        // When
        val foods = dao.getFoodsForDate(day + 1, month, year).first()

        // Then
        assertThat(foods).isEmpty()
    }

    @Test
    fun getTrackedFoodsForCorrectDate_mustReturnTrackedFoodForSameDate() = runBlocking {
        // Given
        val day = 9
        val month = 10
        val year = 1991
        val trackedFood = TrackedFoodEntity(
            id = 1,
            name = "food",
            carbs = 123,
            proteins = 123,
            fats = 123,
            calories = 123,
            type = "type",
            imageUrl = "test.com/1.png",
            amount = 123,
            dayOfMonth = day,
            month = month,
            year = year,
        )
        dao.insertTrackedFood(trackedFood)
        dao.insertTrackedFood(trackedFood.copy(id = 2))
        dao.insertTrackedFood(trackedFood.copy(id = 3, dayOfMonth = day + 1))

        // When
        val foods = dao.getFoodsForDate(day, month, year).first()

        // Then
        assertThat(foods).isNotEmpty()
        assertThat(
            foods.filter { it.dayOfMonth == day && it.month == month && it.year == year }.size
        ).isEqualTo(foods.size)
    }
}