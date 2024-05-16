package com.calorietracker.core.domain.use_case

import com.calorietracker.core.R
import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.utils.UiText

class ValidateWeight {
    operator fun invoke(weight: String): ValidationResult<Float> {
        val weightNumber = weight.toFloatOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_weight_cant_be_empty))
        }

        if (weightNumber == 0f)
            return ValidationResult.Error(UiText.StringResource(R.string.error_weight_cant_be_zero))

        return ValidationResult.Success(weightNumber)
    }
}