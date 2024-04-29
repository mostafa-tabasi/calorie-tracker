package com.calorietracker.onboarding_presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.R
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.usecases.FilterOutNumber
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.UiText
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
            val weightNumber = weight.toFloatOrNull() ?: run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.error_weight_cant_be_empty)
                    )
                )
                return@launch
            }

            if (weightNumber == 0f) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.error_weight_cant_be_zero)
                    )
                )
                return@launch
            }

            preferences.saveWeight(weightNumber)
            _uiEvent.send(UiEvent.Navigate(Route.ACTIVITY))
        }
    }
}