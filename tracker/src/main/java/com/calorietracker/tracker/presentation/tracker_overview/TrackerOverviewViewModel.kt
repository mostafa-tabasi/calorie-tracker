package com.calorietracker.tracker.presentation.tracker_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.navigation.Route
import com.calorietracker.tracker.domain.model.MealType
import com.calorietracker.tracker.domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    preferences: Preferences,
    private val trackerUseCases: TrackerUseCases,
) : ViewModel() {

    var state by mutableStateOf(TrackerOverviewState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getFoodsForDateJob: Job? = null

    init {
        preferences.saveShouldShowOnboarding(false)
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when (event) {
            is TrackerOverviewEvent.OnAddFoodClick ->
                redirectToSearchFoodScreen(event.meal.mealType)

            is TrackerOverviewEvent.OnDeleteTrackedFoodClick ->
                viewModelScope.launch {
                    trackerUseCases.deleteTrackedFood(event.trackedFood)
                    refreshFoods()
                }

            is TrackerOverviewEvent.OnNextDayClick -> {
                increaseDay()
                refreshFoods()
            }

            is TrackerOverviewEvent.OnPreviousDayClick -> {
                decreaseDay()
                refreshFoods()
            }

            is TrackerOverviewEvent.OnToggleMealClick ->
                toggleMeal(event.meal)
        }
    }

    private fun redirectToSearchFoodScreen(mealType: MealType) {
        viewModelScope.launch {
            _uiEvent.send(
                UiEvent.Navigate(
                    route = Route.SEARCH
                            + "/${mealType.name}"
                            + "/${state.date.dayOfMonth}"
                            + "/${state.date.monthValue}"
                            + "/${state.date.year}"
                )
            )
        }
    }

    private fun increaseDay() {
        state = state.copy(
            date = state.date.plusDays(1)
        )
    }

    private fun decreaseDay() {
        state = state.copy(
            date = state.date.minusDays(1)
        )
    }

    private fun toggleMeal(meal: Meal) {
        state = state.copy(
            meals = state.meals.map {
                if (it.mealType != meal.mealType) it
                else it.copy(isExpanded = !it.isExpanded)
            }
        )
    }

    private fun refreshFoods() {
        getFoodsForDateJob?.cancel()
        getFoodsForDateJob = trackerUseCases
            .getFoodsForDate(state.date)
            .onEach { foods ->
                val nutrientsResult = trackerUseCases.calculateMealNutrients(foods)
                state = state.copy(
                    totalCarbs = nutrientsResult.totalCarbs,
                    totalProteins = nutrientsResult.totalProteins,
                    totalFats = nutrientsResult.totalFats,
                    totalCalories = nutrientsResult.totalCalories,
                    carbsGoal = nutrientsResult.carbsGoal,
                    proteinsGoal = nutrientsResult.proteinsGoal,
                    fatsGoal = nutrientsResult.fatsGoal,
                    caloriesGoal = nutrientsResult.caloriesGoal,
                    trackedFoods = foods,
                    meals = state.meals.map {
                        val mealNutrients = nutrientsResult.mealNutrients[it.mealType]
                            ?: return@map it.copy(
                                carbs = 0,
                                proteins = 0,
                                fats = 0,
                                calories = 0,
                            )

                        it.copy(
                            carbs = mealNutrients.carbs,
                            proteins = mealNutrients.proteins,
                            fats = mealNutrients.fats,
                            calories = mealNutrients.calories,
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}
