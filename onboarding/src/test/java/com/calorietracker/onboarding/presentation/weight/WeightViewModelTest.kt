package com.calorietracker.onboarding.presentation.weight

import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.domain.use_case.ValidateWeight
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.UiText
import com.calorietracker.onboarding.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class WeightViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var preferences: Preferences
    private lateinit var filterOutNumber: FilterOutNumber
    private lateinit var validateWeight: ValidateWeight
    private lateinit var viewModel: WeightViewModel

    @Before
    fun setUp() {
        preferences = mock(Preferences::class.java)
        filterOutNumber = mock(FilterOutNumber::class.java)
        validateWeight = mock(ValidateWeight::class.java)
        viewModel = WeightViewModel(preferences, filterOutNumber, validateWeight)
    }

    @Test
    fun `onWeightChange must set the weight in state`() {
        // Initial check
        assertThat(viewModel.weight).isEqualTo("0.0")

        // Given
        val weight = "62"
        `when`(filterOutNumber.invoke(anyString(), anyInt(), anyBoolean(), anyBoolean()))
            .thenReturn(weight)

        // When
        viewModel.onWeightChange(weight)

        // Then
        assertThat(viewModel.weight).isEqualTo(weight)
    }

    @Test
    fun `onNextClick with invalid weight must send snackbar event`() = runTest {
        // Given
        val errorMessage = UiText.DynamicString("error")
        `when`(validateWeight(anyString()))
            .thenReturn(ValidationResult.Error(errorMessage))

        // When
        viewModel.onNextClick()

        // Then
        verify(validateWeight).invoke(anyString())
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.ShowSnackbar(errorMessage))
    }

    @Test
    fun `onNextClick with valid weight must save it and send a navigate event`() = runTest {
        // Given
        val weight = 62f
        `when`(validateWeight(anyString())).thenReturn(ValidationResult.Success(weight))

        // When
        viewModel.onNextClick()

        // Then
        verify(validateWeight).invoke(anyString())
        verify(preferences).saveWeight(weight)
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.NavigateToNextScreen)
    }
}