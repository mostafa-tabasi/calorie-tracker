package com.calorietracker.core.domain.use_case

import com.calorietracker.core.domain.model.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateWeightTest {

    private lateinit var validateWeight: ValidateWeight

    @Before
    fun setUp() {
        validateWeight = ValidateWeight()
    }

    @Test
    fun `empty weight value, expect error`() {
        val emptyWeight = ""
        val result = validateWeight(emptyWeight)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `invalid weight value, expect error`() {
        val invalidWeight = "abc"
        val result = validateWeight.invoke(invalidWeight)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `zero weight value, expect error`() {
        val zeroWeight = "0"
        val zeroWeightButDecimal = "0.0"

        val resultWithZero = validateWeight(zeroWeight)
        val resultWithZeroDecimal = validateWeight(zeroWeightButDecimal)

        assertThat(resultWithZero).isInstanceOf(ValidationResult.Error::class.java)
        assertThat(resultWithZeroDecimal).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `minus weight value, expect error`() {
        val minusWeight = "-70"
        val result = validateWeight(minusWeight)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `valid weight value but with space, expect success`() {
        val validWeight = " 7 0"
        val result = validateWeight(validWeight)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `valid weight value, expect success`() {
        val validWeight = "70"
        val result = validateWeight(validWeight)
        assertThat(result).isInstanceOf(ValidationResult.Success::class.java)
    }
}