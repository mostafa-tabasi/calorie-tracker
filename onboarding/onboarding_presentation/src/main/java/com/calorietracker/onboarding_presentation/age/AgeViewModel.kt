package com.calorietracker.onboarding_presentation.age

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.domain.models.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.usecases.FilterOutNumber
import com.calorietracker.core.domain.usecases.ValidateNumber
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgeViewModel @Inject constructor(
    private var preferences: Preferences,
    private var filterOutNumber: FilterOutNumber,
    private var validateNumber: ValidateNumber,
) : ViewModel() {

    var age by mutableStateOf("0")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAgeChange(value: String) {
        age = filterOutNumber(
            value,
            maxLength = 3,
        )
    }

    fun onNextClick() {
        viewModelScope.launch {
            validateNumber.isAgeValid(age).run {
                when (this) {
                    is ValidationResult.Fail -> _uiEvent.send(UiEvent.ShowSnackbar(message))
                    is ValidationResult.Success -> {
                        preferences.saveAge(number)
                        _uiEvent.send(UiEvent.Navigate(Route.HEIGHT))
                    }
                }
            }
        }
    }
}