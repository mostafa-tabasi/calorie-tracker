package com.calorietracker.core.domain.use_case

import com.calorietracker.core.R
import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.utils.UiText

class ValidateAge {
    operator fun invoke(age: String): ValidationResult<Int> {
        val ageNumber = age.toIntOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_age_cant_be_empty))
        }
        if (ageNumber == 0)
            return ValidationResult.Error(UiText.StringResource(R.string.error_age_cant_be_zero))

        return ValidationResult.Success(ageNumber)
    }
}