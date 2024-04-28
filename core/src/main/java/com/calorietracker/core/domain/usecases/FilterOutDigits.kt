package com.calorietracker.core.domain.usecases

class FilterOutDigits {
    operator fun invoke(value: String): String {
        return value.filter { it.isDigit() }
    }
}