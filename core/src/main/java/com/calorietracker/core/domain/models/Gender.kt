package com.calorietracker.core.domain.models

sealed class Gender(val name: String) {
    data object Male : Gender("male")
    data object Female : Gender("female")

    companion object {
        fun fromString(name: String): Gender {
            return when (name) {
                "male" -> Male
                "female" -> Female
                else -> Male
            }
        }
    }
}