package com.calorietracker.core.domain.models

import com.calorietracker.core.utils.UiText

sealed class ValidationResult<out T> {
    data class Fail(val message: UiText) : ValidationResult<Nothing>()
    data class Success<T>(val number: T) : ValidationResult<T>()
}