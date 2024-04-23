package com.calorietracker.core.domain.models

sealed class ActivityLevel(val name: String) {
    data object Low : ActivityLevel("low")
    data object Medium : ActivityLevel("medium")
    data object High : ActivityLevel("high")
    data object Undefined : ActivityLevel("undefined")

    companion object {
        fun fromString(name: String): ActivityLevel {
            return when (name) {
                "low" -> Low
                "medium" -> Medium
                "high" -> High
                else -> Undefined
            }
        }
    }
}