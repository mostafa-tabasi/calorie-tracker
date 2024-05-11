package com.calorietracker.onboarding.domain.use_cases

import com.calorietracker.core.R
import com.calorietracker.core.domain.model.ValidationResult
import com.calorietracker.core.utils.UiText

class ValidateNutrients {

    operator fun invoke(
        carbsRatioText: String,
        proteinsRatioText: String,
        fatsRatioText: String,
    ): ValidationResult<Nutrients> {
        val carbsRatio = carbsRatioText.toIntOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_carbs_cant_be_empty))
        }
        val proteinsRatio = proteinsRatioText.toIntOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_proteins_cant_be_empty))
        }
        val fatsRatio = fatsRatioText.toIntOrNull() ?: run {
            return ValidationResult.Error(UiText.StringResource(R.string.error_fats_cant_be_empty))
        }

        if (carbsRatio + proteinsRatio + fatsRatio != 100)
            return ValidationResult.Error(UiText.StringResource(R.string.error_not_100_percent))

        return ValidationResult.Success(
            Nutrients(
                carbsRatio / 100f,
                proteinsRatio / 100f,
                fatsRatio / 100f,
            )
        )
    }

    data class Nutrients(
        val carbsRatio: Float,
        val proteinsRatio: Float,
        val fatsRatio: Float,
    )
}