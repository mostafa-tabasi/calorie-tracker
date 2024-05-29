package com.calorietracker.onboarding.presentation.height

import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.domain.use_case.ValidateHeight
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.UiText
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class HeightViewModelTest {

    private lateinit var preferences: Preferences
    private lateinit var filterOutNumber: FilterOutNumber
    private lateinit var validateHeight: ValidateHeight
    private lateinit var viewModel: HeightViewModel

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        filterOutNumber = mockk(relaxed = true)
        validateHeight = mockk(relaxed = true)
        viewModel = HeightViewModel(preferences, filterOutNumber, validateHeight)
    }

    @Test
    fun `onWeightChange must set the height in state`() {
        // Initial check
        assertThat(viewModel.height).isEqualTo("0")

        // Given
        val height = "174"
        coEvery { filterOutNumber(any(), any()) } returns height

        // When
        viewModel.onHeightChange(height)

        // Then
        coVerify { filterOutNumber(height, any()) }
        assertThat(viewModel.height).isEqualTo(height)
    }

    @Test
    fun `onNextClick with invalid height must send show snackbar event`() = runBlocking {
        // Given
        val validationResultMessage = UiText.DynamicString("message")
        coEvery { validateHeight(any()) } returns ValidationResult.Error(validationResultMessage)

        // When
        viewModel.onNextClick()

        // Then
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.ShowSnackbar(validationResultMessage))
    }

    @Test
    fun `onNextClick with valid height must save it and send navigate event`() = runBlocking {
        // Given
        val height = 174
        coEvery { filterOutNumber(any(), any()) } returns height.toString()
        coEvery { validateHeight(any()) } returns ValidationResult.Success(height)
        viewModel.onHeightChange(height.toString())

        // When
        viewModel.onNextClick()

        // Then
        coVerify { validateHeight(height.toString()) }
        coVerify { preferences.saveHeight(height) }
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.NavigateToNextScreen)
    }
}