package com.calorietracker.onboarding.presentation.goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorietracker.core.domain.model.Goal
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {

    var selectedGoal by mutableStateOf<Goal>(Goal.KeepWeight)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onGoalClick(goal: Goal) {
        selectedGoal = goal
    }

    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveGoal(selectedGoal)
            _uiEvent.send(UiEvent.NavigateToNextScreen)
        }
    }
}