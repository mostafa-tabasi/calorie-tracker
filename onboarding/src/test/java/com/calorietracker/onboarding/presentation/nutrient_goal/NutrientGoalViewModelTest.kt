package com.calorietracker.onboarding.presentation.nutrient_goal

import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core.utils.UiText
import com.calorietracker.onboarding.domain.use_cases.ValidateNutrients
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class NutrientGoalViewModelTest {

    private lateinit var preferences: Preferences
    private lateinit var filterOutNumber: FilterOutNumber
    private lateinit var validateNutrients: ValidateNutrients
    private lateinit var viewModel: NutrientGoalViewModel

    @Before
    fun setUp() {
        preferences = mock(Preferences::class.java)
        filterOutNumber = mock(FilterOutNumber::class.java)
        validateNutrients = mock(ValidateNutrients::class.java)
        viewModel = NutrientGoalViewModel(preferences, filterOutNumber, validateNutrients)
    }

    @Test
    fun `OnCarbsRatioChange event must keep the amount in state`() {
        // Initial state check
        assertThat(viewModel.state.carbsRatio).isEqualTo("0")

        // Given
        val carbsRatio = "30"
        `when`(filterOutNumber(anyString(), anyInt(), anyBoolean(), anyBoolean()))
            .thenReturn(carbsRatio)

        // When
        viewModel.onEvent(NutrientGoalEvent.OnCarbsRatioChange(carbsRatio))

        // Then
        verify(filterOutNumber).invoke(carbsRatio, 3, canBeZero = true, canBeDecimal = false)
        assertThat(viewModel.state.carbsRatio).isEqualTo(carbsRatio)
        assertThat(viewModel.state.proteinsRatio).isEqualTo("0")
        assertThat(viewModel.state.fatsRatio).isEqualTo("0")
    }

    @Test
    fun `OnProteinsRatioChange event must keep the amount in state`() {
        // Initial state check
        assertThat(viewModel.state.proteinsRatio).isEqualTo("0")

        // Given
        val proteinRatio = "30"
        `when`(filterOutNumber(anyString(), anyInt(), anyBoolean(), anyBoolean()))
            .thenReturn(proteinRatio)

        // When
        viewModel.onEvent(NutrientGoalEvent.OnProteinsRatioChange(proteinRatio))

        // Then
        verify(filterOutNumber).invoke(proteinRatio, 3, canBeZero = true, canBeDecimal = false)
        assertThat(viewModel.state.carbsRatio).isEqualTo("0")
        assertThat(viewModel.state.proteinsRatio).isEqualTo(proteinRatio)
        assertThat(viewModel.state.fatsRatio).isEqualTo("0")
    }

    @Test
    fun `OnFatsRatioChange event must keep the amount in state`() {
        // Initial state check
        assertThat(viewModel.state.fatsRatio).isEqualTo("0")

        // Given
        val fatRatio = "30"
        `when`(filterOutNumber(anyString(), anyInt(), anyBoolean(), anyBoolean()))
            .thenReturn(fatRatio)

        // When
        viewModel.onEvent(NutrientGoalEvent.OnFatsRatioChange(fatRatio))

        // Then
        verify(filterOutNumber).invoke(fatRatio, 3, canBeZero = true, canBeDecimal = false)
        assertThat(viewModel.state.carbsRatio).isEqualTo("0")
        assertThat(viewModel.state.proteinsRatio).isEqualTo("0")
        assertThat(viewModel.state.fatsRatio).isEqualTo(fatRatio)
    }

    @Test
    fun `OnNextClick with invalid inputs must send show snackbar event`() = runBlocking {
        // Given
        val validationResultMessage = UiText.DynamicString("message")
        `when`(validateNutrients(anyString(), anyString(), anyString()))
            .thenReturn(ValidationResult.Error(validationResultMessage))

        // When
        viewModel.onEvent(NutrientGoalEvent.OnNextClick)

        // Then
        verify(validateNutrients).invoke(anyString(), anyString(), anyString())
        val event = viewModel.uiEvent.first()
        assertThat(event).isEqualTo(UiEvent.ShowSnackbar(validationResultMessage))
    }

    @Test
    fun `OnNextClick with valid inputs must save the amounts and send navigate event`() =
        runBlocking {
            // Given
            val carbsRatio = 0.3f
            val proteinRatio = 0.4f
            val fatRatio = 0.3f
            `when`(validateNutrients(anyString(), anyString(), anyString()))
                .thenReturn(
                    ValidationResult.Success(
                        ValidateNutrients.Nutrients(carbsRatio, proteinRatio, fatRatio)
                    )
                )

            // When
            viewModel.onEvent(NutrientGoalEvent.OnNextClick)

            // Then
            verify(validateNutrients).invoke(anyString(), anyString(), anyString())
            verify(preferences).saveCarbRatio(carbsRatio)
            verify(preferences).saveProteinRatio(proteinRatio)
            verify(preferences).saveFatRatio(fatRatio)
            val event = viewModel.uiEvent.first()
            assertThat(event).isEqualTo(UiEvent.NavigateToNextScreen)
        }
}