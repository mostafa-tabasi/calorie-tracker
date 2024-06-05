package com.calorietracker.onboarding.presentation.goal

import com.calorietracker.core.domain.model.Goal
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.onboarding.MainDispatcherRule
import com.google.common.truth.Truth
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GoalViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var preferences: Preferences
    private lateinit var viewModel: GoalViewModel

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        viewModel = GoalViewModel(preferences)
    }

    @Test
    fun `onGoalClick must set the selected goal in state`() {
        // Initial check
        Truth.assertThat(viewModel.selectedGoal).isEqualTo(Goal.KeepWeight)

        // When
        viewModel.onGoalClick(Goal.GainWeight)

        // Then
        Truth.assertThat(viewModel.selectedGoal).isEqualTo(Goal.GainWeight)
    }

    @Test
    fun `onNextClick must save the selected goal and send navigate event`() = runTest {
        // Given
        viewModel.onGoalClick(Goal.GainWeight)

        // When
        viewModel.onNextClick()

        // Then
        coVerify { preferences.saveGoal(Goal.GainWeight) }
        val event = viewModel.uiEvent.first()
        Truth.assertThat(event).isEqualTo(UiEvent.NavigateToNextScreen)
    }
}