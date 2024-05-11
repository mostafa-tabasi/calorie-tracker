package com.calorietracker.onboarding.presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.navigation.Route
import com.calorietracker.onboarding.domain.use_cases.ValidateNutrients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private var preferences: Preferences,
    private var filterOutNumber: FilterOutNumber,
    private var validateNutrients: ValidateNutrients,
) : ViewModel() {

    var state by mutableStateOf(NutrientGoalState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: NutrientGoalEvent) {
        when (event) {
            is NutrientGoalEvent.OnCarbsRatioChange -> state =
                state.copy(carbsRatio = filterOutNumber(event.ratio, 3, canBeZero = true))

            is NutrientGoalEvent.OnFatsRatioChange -> state =
                state.copy(fatsRatio = filterOutNumber(event.ratio, 3, canBeZero = true))

            is NutrientGoalEvent.OnProteinsRatioChange -> state =
                state.copy(proteinsRatio = filterOutNumber(event.ratio, 3, canBeZero = true))

            is NutrientGoalEvent.OnNextClick -> {
                viewModelScope.launch {
                    val result = validateNutrients(
                        carbsRatioText = state.carbsRatio,
                        proteinsRatioText = state.proteinsRatio,
                        fatsRatioText = state.fatsRatio,
                    )
                    when (result) {
                        is ValidationResult.Error -> _uiEvent.send(UiEvent.ShowSnackbar(result.message))
                        is ValidationResult.Success -> {
                            preferences.saveCarbRatio(result.data.carbsRatio)
                            preferences.saveProteinRatio(result.data.proteinsRatio)
                            preferences.saveFatRatio(result.data.fatsRatio)
                            _uiEvent.send(UiEvent.Navigate(Route.TRACKER_OVERVIEW))
                        }
                    }
                }
            }
        }
    }

}