package com.calorietracker.onboarding.presentation.age

import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.domain.use_case.ValidateAge
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

class AgeViewModelTest {

    private lateinit var preferences: Preferences
    private lateinit var filterOutNumber: FilterOutNumber
    private lateinit var validateAge: ValidateAge

    private lateinit var viewModel: AgeViewModel


    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        filterOutNumber = mockk(relaxed = true)
        validateAge = mockk(relaxed = true)

        viewModel = AgeViewModel(
            preferences,
            filterOutNumber,
            validateAge,
        )
    }

    @Test
    fun `on age changed, age must be set in the state`() {
        // Initial check
        assertThat(viewModel.age).isEqualTo("0")

        // Given
        val age = "32"
        coEvery { filterOutNumber(any(), any()) } returns age

        // When
        viewModel.onAgeChange(age)

        // Then
        coVerify { filterOutNumber(age, any()) }
        assertThat(viewModel.age).isEqualTo(age)
    }

    @Test
    fun `onNextClick with invalid age must send error snackbar event`() = runBlocking {
        // Given
        val validationResultMessage = UiText.DynamicString("Error Message")
        coEvery { validateAge(any()) } returns ValidationResult.Error(validationResultMessage)

        // When
        viewModel.onNextClick()

        // Then
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.ShowSnackbar(validationResultMessage))
    }

    @Test
    fun `onNextClick with valid age must send navigate event`() = runBlocking {
        // Given
        val age = 35
        coEvery { validateAge(any()) } returns ValidationResult.Success(age)

        // When
        viewModel.onNextClick()

        // Then
        coVerify { preferences.saveAge(age) }
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.NavigateToNextScreen)
    }
}