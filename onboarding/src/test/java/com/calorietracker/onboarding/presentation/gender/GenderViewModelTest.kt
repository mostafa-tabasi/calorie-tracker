package com.calorietracker.onboarding.presentation.gender

import com.calorietracker.core.domain.model.Gender
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.utils.UiEvent
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GenderViewModelTest {

    private lateinit var preferences: Preferences
    private lateinit var viewModel: GenderViewModel

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        viewModel = GenderViewModel(preferences)
    }

    @Test
    fun `onGenderClick must set the selected gender in state`() {
        // Initial check
        assertThat(viewModel.selectedGender).isEqualTo(Gender.Male)

        // When
        viewModel.onGenderClick(Gender.Female)

        // Then
        assertThat(viewModel.selectedGender).isEqualTo(Gender.Female)
    }

    @Test
    fun `onNextClick must save the selected gender and send navigate event`() = runBlocking {
        // Given
        viewModel.onGenderClick(Gender.Female)

        // When
        viewModel.onNextClick()

        // Then
        coVerify { preferences.saveGender(Gender.Female) }
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.NavigateToNextScreen)
    }
}