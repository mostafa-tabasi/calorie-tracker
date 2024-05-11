package com.calorietracker.tracker.domain.use_case

import com.calorietracker.core.domain.model.ActivityLevel
import com.calorietracker.core.domain.model.Gender
import com.calorietracker.core.domain.model.Goal
import com.calorietracker.core.domain.model.UserInfo
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.model.TrackedFood
import kotlin.math.roundToInt

class CalculateMealNutrients(
    private val preferences: Preferences,
) {

    operator fun invoke(trackedFoods: List<TrackedFood>): Result {
        val allNutrients = trackedFoods
            .groupBy { it.mealType }
            .mapValues { entry ->
                val type = entry.key
                val foods = entry.value
                MealNutrients(
                    mealType = type,
                    carbs = foods.sumOf { it.carbs },
                    proteins = foods.sumOf { it.proteins },
                    fats = foods.sumOf { it.fats },
                    calories = foods.sumOf { it.calories },
                )
            }

        val totalCarbs = allNutrients.values.sumOf { it.carbs }
        val totalProteins = allNutrients.values.sumOf { it.proteins }
        val totalFats = allNutrients.values.sumOf { it.fats }
        val totalCalories = allNutrients.values.sumOf { it.calories }

        val userInfo = preferences.loadUserInfo()
        val calorieGoal = dailyCalorieRequirement(userInfo)
        //                                                   Carbohydrates provide 4 calories per gram
        //                                                   We need the amount for 1 calorie
        val carbsGoal = ((calorieGoal * userInfo.carbRatio) / 4f).roundToInt()
        //                                                          Protein provide 4 calories per gram
        val proteinsGoal = ((calorieGoal * userInfo.proteinRatio) / 4f).roundToInt()
        //                                                  Fat provide 9 calories per gram
        val fatsGoal = ((calorieGoal * userInfo.fatRatio) / 9f).roundToInt()

        return Result(
            carbsGoal = carbsGoal,
            proteinsGoal = proteinsGoal,
            fatsGoal = fatsGoal,
            caloriesGoal = calorieGoal,
            totalCarbs = totalCarbs,
            totalProteins = totalProteins,
            totalFats = totalFats,
            totalCalories = totalCalories,
            mealNutrients = allNutrients,
        )
    }

    /**
     * Calculates Basal Metabolic Rate [bmr] based on the [userInfo].
     * @return the user BMR.
     */
    private fun bmr(userInfo: UserInfo): Int {
        return when (userInfo.gender) {
            is Gender.Male ->
                (66.47f
                        + (13.75f * userInfo.weight)
                        + (5f * userInfo.height)
                        - (6.75f * userInfo.age)
                        ).roundToInt()

            is Gender.Female ->
                (655.1f
                        + (9.56f * userInfo.weight)
                        + (1.84f * userInfo.height)
                        - (4.67f * userInfo.age)
                        ).roundToInt()
        }
    }

    private fun dailyCalorieRequirement(userInfo: UserInfo): Int {
        val activityFactor = when (userInfo.activityLevel) {
            ActivityLevel.Low -> 1.2f
            ActivityLevel.Medium -> 1.3f
            ActivityLevel.High -> 1.4f
        }
        val calorieExtra = when (userInfo.goal) {
            Goal.LoseWeight -> -500
            Goal.KeepWeight -> 0
            Goal.GainWeight -> +500
        }
        return ((bmr(userInfo) * activityFactor) + calorieExtra).roundToInt()
    }

    data class MealNutrients(
        val carbs: Int,
        val proteins: Int,
        val fats: Int,
        val calories: Int,
        val mealType: MealType,
    )

    data class Result(
        val carbsGoal: Int,
        val proteinsGoal: Int,
        val fatsGoal: Int,
        val caloriesGoal: Int,
        val totalCarbs: Int,
        val totalProteins: Int,
        val totalFats: Int,
        val totalCalories: Int,
        val mealNutrients: Map<MealType, MealNutrients>,
    )
}