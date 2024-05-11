package com.calorietracker.core.domain.model

import com.calorietracker.core.utils.UiText

sealed class ValidationResult<out T> {
    data class Error(val message: UiText) : ValidationResult<Nothing>()
    data class Success<T>(val data: T) : ValidationResult<T>()
}