package com.calorietracker.core.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class FilterOutNumberTest {

    private lateinit var filterOutNumber: FilterOutNumber

    @Before
    fun setUp() {
        filterOutNumber = FilterOutNumber()
    }

    @Test
    fun `value with character and less length than max length, expect to get the exact number amount`() {
        val maxLength = 3
        val value = "1a2b"
        val filteredValue = filterOutNumber(value, maxLength)
        assertThat(filteredValue).isEqualTo("12")
    }

    @Test
    fun `value with character and equal length of max length, expect to get the exact number amount`() {
        val maxLength = 3
        val value = "1a2b3c"
        val filteredValue = filterOutNumber(value, maxLength)
        assertThat(filteredValue).isEqualTo("123")
    }

    @Test
    fun `value with character and more length than max length, expect to get the first numbers as long as max length`() {
        val maxLength = 3
        val value = "1a2b3c4567"
        val filteredValue = filterOutNumber(value, maxLength)
        assertThat(filteredValue).isEqualTo("123")
    }

    @Test
    fun `decimal value while it can't be, expect to get non decimal`() {
        val maxLength = 3
        val value = "1.5"
        val filteredValue = filterOutNumber(value, maxLength, canBeDecimal = false)
        assertThat(filteredValue).isEqualTo("15")
    }

    @Test
    fun `decimal value while it can be, expect to get decimal`() {
        val maxLength = 3
        val value = "1.5"
        val filteredValue = filterOutNumber(value, maxLength, canBeDecimal = true)
        assertThat(filteredValue).isEqualTo("1.5")
    }

    @Test
    fun `zero value while it can't be, expect to get nothing`() {
        val maxLength = 3
        val value = "0"
        val filteredValue = filterOutNumber(value, maxLength, canBeZero = false)
        assertThat(filteredValue).isEqualTo("")
    }

    @Test
    fun `zero value while it can be, expect to get zero`() {
        val maxLength = 3
        val value = "0"
        val filteredValue = filterOutNumber(value, maxLength, canBeZero = true)
        assertThat(filteredValue).isEqualTo("0")
    }
}