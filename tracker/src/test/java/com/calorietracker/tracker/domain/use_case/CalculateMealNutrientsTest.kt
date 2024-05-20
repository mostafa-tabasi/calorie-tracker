package com.calorietracker.tracker.domain.use_case

import com.calorietracker.core.domain.model.ActivityLevel
import com.calorietracker.core.domain.model.Gender
import com.calorietracker.core.domain.model.Goal
import com.calorietracker.core.domain.model.UserInfo
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackedFood
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

class CalculateMealNutrientsTest {

    private lateinit var calculateMealNutrients: CalculateMealNutrients
    private lateinit var trackedFood: List<TrackedFood>

    @Before
    fun setUp() {
        val preferences = mockk<Preferences>(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 32,
            weight = 62f,
            height = 174,
            activityLevel = ActivityLevel.High,
            goal = Goal.GainWeight,
            carbRatio = 0.3f,
            proteinRatio = 0.5f,
            fatRatio = 0.2f,
        )
        calculateMealNutrients = CalculateMealNutrients(preferences)

        trackedFood = (1..30).map {
            TrackedFood(
                name = "food",
                carbs = Random.nextInt(100),
                proteins = Random.nextInt(100),
                fats = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(1000)
            )
        }
    }

    @Test
    fun `calories for breakfast calculated properly`() {
        val calculatedNutrients = calculateMealNutrients(trackedFood)

        val actualCaloriesForBreakfast = trackedFood
            .filter { it.mealType == MealType.Breakfast }
            .sumOf { it.calories }

        assertThat(actualCaloriesForBreakfast)
            .isEqualTo(calculatedNutrients.mealNutrients[MealType.Breakfast]?.calories)
    }

    @Test
    fun `calories for lunch calculated properly`() {
        val calculatedNutrients = calculateMealNutrients(trackedFood)

        val actualCaloriesForLunch = trackedFood
            .filter { it.mealType == MealType.Lunch }
            .sumOf { it.calories }

        assertThat(actualCaloriesForLunch)
            .isEqualTo(calculatedNutrients.mealNutrients[MealType.Lunch]?.calories)
    }

    @Test
    fun `calories for dinner calculated properly`() {
        val calculatedNutrients = calculateMealNutrients(trackedFood)

        val actualCaloriesForDinner = trackedFood
            .filter { it.mealType == MealType.Dinner }
            .sumOf { it.calories }

        assertThat(actualCaloriesForDinner)
            .isEqualTo(calculatedNutrients.mealNutrients[MealType.Dinner]?.calories)
    }

    @Test
    fun `calories for snack calculated properly`() {
        val calculatedNutrients = calculateMealNutrients(trackedFood)

        val actualCaloriesForSnack = trackedFood
            .filter { it.mealType == MealType.Snack }
            .sumOf { it.calories }

        assertThat(actualCaloriesForSnack)
            .isEqualTo(calculatedNutrients.mealNutrients[MealType.Snack]?.calories)
    }
}