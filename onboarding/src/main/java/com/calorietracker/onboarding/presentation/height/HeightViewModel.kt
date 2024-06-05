package com.calorietracker.onboarding.presentation.height

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.domain.use_case.ValidateHeight
import com.calorietracker.core.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeightViewModel @Inject constructor(
    private var preferences: Preferences,
    private var filterOutNumber: FilterOutNumber,
    private var validateHeight: ValidateHeight,
) : ViewModel() {

    var height by mutableStateOf("0")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onHeightChange(value: String) {
        height = filterOutNumber(
            value,
            maxLength = 3,
        )
    }

    fun onNextClick() {
        viewModelScope.launch {
            validateHeight(height).run {
                when (this) {
                    is ValidationResult.Error -> _uiEvent.send(UiEvent.ShowSnackbar(message))
                    is ValidationResult.Success -> {
                        preferences.saveHeight(data)
                        _uiEvent.send(UiEvent.NavigateToNextScreen)
                    }
                }
            }
        }
    }
}