package com.calorietracker.tracker.domain.use_case

import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackableFood
import com.calorietracker.tracker.domain.model.TrackedFood
import com.calorietracker.tracker.domain.repository.TrackerRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFoodTest {

    private lateinit var repository: TrackerRepository
    private lateinit var trackFood: TrackFood

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        trackFood = TrackFood(repository)
    }

    @Test
    fun `use case must insert the correctly transformed tracked food`() = runBlocking {
        // Given
        val trackableFood = TrackableFood(
            name = "Food",
            imageUrl = "test.com/1.png",
            caloriesPer100g = 120,
            carbsPer100g = 35,
            proteinsPer100g = 50,
            fatsPer100g = 15,
        )
        val amount = 300
        val mealType = MealType.Breakfast
        val date = LocalDate.now()

        val slot = slot<TrackedFood>()
        coEvery { repository.insertTrackedFood(capture(slot)) } returns Unit

        // When
        trackFood(trackableFood, amount, mealType, date)

        // Then
        coVerify { repository.insertTrackedFood(any()) }

        val trackFood = slot.captured
        assertThat(trackFood.name).isEqualTo("Food")
        assertThat(trackFood.imageUrl).isEqualTo("test.com/1.png")
        assertThat(trackFood.mealType).isEqualTo(MealType.Breakfast)
        assertThat(trackFood.amount).isEqualTo(amount)
        assertThat(trackFood.date).isEqualTo(date)
        assertThat(trackFood.carbs).isEqualTo((35 / 100f * amount).roundToInt())
        assertThat(trackFood.proteins).isEqualTo((50 / 100f * amount).roundToInt())
        assertThat(trackFood.fats).isEqualTo((15 / 100f * amount).roundToInt())
        assertThat(trackFood.calories).isEqualTo((120 / 100f * amount).roundToInt())
    }
}