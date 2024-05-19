package com.calorietracker.core.domain.use_case

import com.calorietracker.core.domain.model.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateHeightTest {

    private lateinit var validateHeight: ValidateHeight

    @Before
    fun setUp() {
        validateHeight = ValidateHeight()
    }

    @Test
    fun `empty height value, expect error`() {
        val invalidHeight = ""
        val result = validateHeight.invoke(invalidHeight)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `invalid height value, expect error`() {
        val invalidHeight = "abc"
        val result = validateHeight.invoke(invalidHeight)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `zero height value, expect error`() {
        val zeroHeight = "0"
        val zeroHeightButDecimal = "0.0"

        val resultWithZero = validateHeight(zeroHeight)
        val resultWithZeroDecimal = validateHeight(zeroHeightButDecimal)

        assertThat(resultWithZero).isInstanceOf(ValidationResult.Error::class.java)
        assertThat(resultWithZeroDecimal).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `minus height value, expect error`() {
        val minusHeight = "-170"
        val result = validateHeight(minusHeight)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `valid height value but with space, expect success`() {
        val validHeight = " 17 0"
        val result = validateHeight(validHeight)
        assertThat(result).isInstanceOf(ValidationResult.Error::class.java)
    }

    @Test
    fun `valid height value, expect success`() {
        val validHeight = "170"
        val result = validateHeight(validHeight)
        assertThat(result).isInstanceOf(ValidationResult.Success::class.java)
    }
}