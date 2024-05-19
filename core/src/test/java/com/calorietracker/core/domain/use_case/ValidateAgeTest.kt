package com.calorietracker.core.domain.use_case

import com.calorietracker.core.domain.model.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateAgeTest {

    private lateinit var validateAge: ValidateAge

    @Before
    fun setUp() {
        validateAge = ValidateAge()
    }

    @Test
    fun `empty age value, expect error`() {
        val emptyAge = ""
        val result = validateAge(emptyAge)

        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `invalid age value, expect error`() {
        val invalidAge = "abc"
        val result = validateAge(invalidAge)

        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `zero year old, expect error`() {
        val zeroYearOld = "0"
        val zeroYearOldButDecimal = "0.0"

        val resultWithZero = validateAge(zeroYearOld)
        val resultWithZeroDecimal = validateAge(zeroYearOldButDecimal)

        assertThat(resultWithZero).isInstanceOf(ValidationResult.Error::class.java)
        assertThat(resultWithZeroDecimal).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `minus year old, expect error`() {
        val minusYearOld = "-7"
        val result = validateAge(minusYearOld)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `valid year value but with space, expect error`() {
        val validYearOld = " 17 "
        val result = validateAge(validYearOld)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `valid year value, expect success`() {
        val validYearOld = "17"
        val result = validateAge(validYearOld)
        assertThat(result).isInstanceOf(ValidationResult.Success::class.java)
    }
}