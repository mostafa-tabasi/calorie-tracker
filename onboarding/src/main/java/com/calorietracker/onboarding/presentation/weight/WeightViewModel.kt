package com.calorietracker.onboarding.presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.domain.models.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_cases.FilterOutNumber
import com.calorietracker.core.domain.use_cases.ValidateNumber
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private var preferences: Preferences,
    private var filterOutNumber: FilterOutNumber,
    private var validateNumber: ValidateNumber,
) : ViewModel() {

    var weight by mutableStateOf("0.0")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onWeightChange(value: String) {
        weight = filterOutNumber(
            value,
            maxLength = 3,
            isDecimal = true,
        )
    }

    fun onNextClick() {
        viewModelScope.launch {
            validateNumber.isWeightValid(weight).run {
                when (this) {
                    is ValidationResult.Error -> _uiEvent.send(UiEvent.ShowSnackbar(message))
                    is ValidationResult.Success -> {
                        preferences.saveWeight(data)
                        _uiEvent.send(UiEvent.Navigate(Route.ACTIVITY))
                    }
                }
            }
        }
    }
}