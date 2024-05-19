package com.calorietracker.core.domain.use_case

import com.calorietracker.core.R
import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.utils.UiText

class ValidateHeight {
    operator fun invoke(height: String): ValidationResult<Int> {
        val heightNumber = height.toIntOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_height_cant_be_empty))
        }

        if (heightNumber <= 0)
            return ValidationResult.Error(UiText.StringResource(R.string.error_height_cant_be_zero))

        return ValidationResult.Success(heightNumber)
    }
}