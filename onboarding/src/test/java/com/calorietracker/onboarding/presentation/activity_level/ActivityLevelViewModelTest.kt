package com.calorietracker.onboarding.presentation.activity_level

import com.calorietracker.core.domain.model.ActivityLevel
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.onboarding.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActivityLevelViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var preferences: Preferences
    private lateinit var viewModel: ActivityLevelViewModel

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        viewModel = ActivityLevelViewModel(preferences)
    }

    @Test
    fun `onActivityLevelClick must set the selected activity in state`() {
        // Initial check
        assertThat(viewModel.selectedLevel).isEqualTo(ActivityLevel.Medium)

        // When
        viewModel.onActivityLevelClick(ActivityLevel.High)

        // Then
        assertThat(viewModel.selectedLevel).isEqualTo(ActivityLevel.High)
    }

    @Test
    fun `onNextClick must save the selected activity and send navigate event`() = runTest {
        // Given
        viewModel.onActivityLevelClick(ActivityLevel.High)

        // When
        viewModel.onNextClick()

        // Then
        coVerify { preferences.saveActivityLevel(ActivityLevel.High) }
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.NavigateToNextScreen)
    }
}