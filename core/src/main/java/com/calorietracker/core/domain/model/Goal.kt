package com.calorietracker.core.domain.model

sealed class Goal(val name: String) {
    data object LoseWeight : Goal("lose_weight")
    data object KeepWeight : Goal("keep_weight")
    data object GainWeight : Goal("gain_weight")

    companion object {
        fun fromString(name: String): Goal {
            return when (name) {
                "lose_weight" -> LoseWeight
                "keep_weight" -> KeepWeight
                "gain_weight" -> GainWeight
                else -> KeepWeight
            }
        }
    }
}