package com.calorietracker.core.domain.use_case

class FilterOutNumber {
    operator fun invoke(
        value: String,
        maxLength: Int,
        isDecimal: Boolean = false,
        canBeZero: Boolean = false,
    ): String {
        if (value.isEmpty()) return ""

        if (!isDecimal) value.filter { it.isDigit() }.also {
            val trimmed = if (!canBeZero) it.trimStart('0') else it
            return trimmed.take(maxLength)
        }

        val splitValue = value.split(".")
        var integralPart = splitValue.getOrElse(0) { "0" }
        if (!canBeZero) integralPart = integralPart.trimStart('0')
        val decimalPart = splitValue.getOrElse(1) { "0" }
        return "${integralPart.take(maxLength)}.${decimalPart.take(2)}"
    }
}