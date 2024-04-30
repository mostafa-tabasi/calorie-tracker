package com.calorietracker.core.domain.use_cases

import com.calorietracker.core.R
import com.calorietracker.core.domain.models.ValidationResult
import com.calorietracker.core.utils.UiText

class ValidateNumber {
    fun isAgeValid(age: String): ValidationResult<Int> {
        val ageNumber = age.toIntOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_age_cant_be_empty))
        }
        if (ageNumber == 0)
            return ValidationResult.Error(UiText.StringResource(R.string.error_age_cant_be_zero))

        return ValidationResult.Success(ageNumber)
    }

    fun isHeightValid(height: String): ValidationResult<Int> {
        val heightNumber = height.toIntOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_height_cant_be_empty))
        }
        if (heightNumber == 0)
            return ValidationResult.Error(UiText.StringResource(R.string.error_height_cant_be_zero))

        return ValidationResult.Success(heightNumber)
    }

    fun isWeightValid(weight: String): ValidationResult<Float> {
        val weightNumber = weight.toFloatOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_weight_cant_be_empty))
        }

        if (weightNumber == 0f)
            return ValidationResult.Error(UiText.StringResource(R.string.error_weight_cant_be_zero))

        return ValidationResult.Success(weightNumber)
    }
}