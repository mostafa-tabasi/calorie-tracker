package com.calorietracker.core.domain.usecases

class FilterOutNumber {
    operator fun invoke(
        value: String,
        maxLength: Int,
        isDecimal: Boolean = false,
    ): String {
        if (value.isEmpty()) return ""

        if (!isDecimal) return value.trimStart('0').filter { it.isDigit() }.take(maxLength)

        val splitValue = value.split(".")
        val integralPart = splitValue.getOrElse(0) { "0" }.trimStart('0').take(maxLength)
        val decimalPart = splitValue.getOrElse(1) { "0" }.take(2)
        return "$integralPart.$decimalPart"
    }
}