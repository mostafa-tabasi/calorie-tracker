package com.calorietracker.onboarding.presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.domain.use_case.ValidateWeight
import com.calorietracker.core.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private var preferences: Preferences,
    private var filterOutNumber: FilterOutNumber,
    private var validateWeight: ValidateWeight,
) : ViewModel() {

    var weight by mutableStateOf("0.0")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onWeightChange(value: String) {
        weight = filterOutNumber(
            value,
            maxLength = 3,
            canBeDecimal = true,
        )
    }

    fun onNextClick() {
        viewModelScope.launch(Dispatchers.IO) {
            validateWeight(weight).run {
                when (this) {
                    is ValidationResult.Error -> _uiEvent.send(UiEvent.ShowSnackbar(message))
                    is ValidationResult.Success -> {
                        preferences.saveWeight(data)
                        _uiEvent.send(UiEvent.NavigateToNextScreen)
                    }
                }
            }
        }
    }
}