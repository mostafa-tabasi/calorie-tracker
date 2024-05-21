package com.calorietracker.tracker.data.repository

import com.calorietracker.tracker.data.remote.OpenFoodApi
import com.calorietracker.tracker.data.remote.dto.Nutriments
import com.calorietracker.tracker.data.remote.dto.Product
import com.calorietracker.tracker.data.remote.dto.SearchDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TrackerRepositoryImplTest {

    private lateinit var repository: TrackerRepositoryImpl
    private lateinit var api: OpenFoodApi

    @Before
    fun setUp() {
        api = mockk(relaxed = true)
        repository = TrackerRepositoryImpl(
            dao = mockk(relaxed = true),
            api = api,
        )
    }

    @Test
    fun `searchFood returns success result on successful API response`() = runBlocking {
        // Given
        val searchDto = SearchDto(
            products = listOf(
                Product(
                    imageFrontThumbUrl = "http://test.com/image1.jpg",
                    nutriments = Nutriments(
                        carbohydrates100g = 10.0,
                        proteins100g = 10.0,
                        fat100g = 10.0,
                        energyKcal100g = 170.0,
                    ),
                    productName = "Test Food 1",
                ),
                Product(
                    imageFrontThumbUrl = "http://test.com/image2.jpg",
                    nutriments = Nutriments(
                        carbohydrates100g = 10.0,
                        proteins100g = 10.0,
                        fat100g = 10.0,
                        energyKcal100g = 170.0,
                    ),
                    productName = "Test Food 2",
                ),
            )
        )
        coEvery { api.searchFood(any(), any(), any()) } returns Response.success(searchDto)

        // When
        val result = repository.searchFood("test", 1, 10)

        // Then
        assertThat(result.isSuccess).isTrue()
        val foods = result.getOrNull()
        assertThat(foods).isNotNull()
        assertThat(foods).hasSize(2)
        coVerify { api.searchFood("test", 1, 10) }
    }

    @Test
    fun `searchFood returns success but with an empty list of items after filtering the result`() =
        runBlocking {
            // Given
            val searchDto = SearchDto(
                products = listOf(
                    Product(
                        imageFrontThumbUrl = "http://test.com/image1.jpg",
                        nutriments = Nutriments(
                            carbohydrates100g = 10.0,
                            proteins100g = 10.0,
                            fat100g = 10.0,
                            energyKcal100g = 0.0,
                        ),
                        productName = "Test Food 1",
                    ),
                    Product(
                        imageFrontThumbUrl = "http://test.com/image2.jpg",
                        nutriments = Nutriments(
                            carbohydrates100g = 10.0,
                            proteins100g = 10.0,
                            fat100g = 10.0,
                            energyKcal100g = 40.0,
                        ),
                        productName = "Test Food 2",
                    ),
                )
            )
            coEvery { api.searchFood(any(), any(), any()) } returns Response.success(searchDto)

            // When
            val result = repository.searchFood("test", 1, 10)

            // Then
            assertThat(result.isSuccess).isTrue()
            val foods = result.getOrNull()
            assertThat(foods).isNotNull()
            assertThat(foods).hasSize(0)
            coVerify { api.searchFood("test", 1, 10) }
        }

    @Test
    fun `searchFood returns failure when API call is unsuccessful`() = runBlocking {
        // Given
        val response = Response.error<SearchDto>(
            404,
            "Not found".toResponseBody(null)
        )
        coEvery { api.searchFood(any(), any(), any()) } returns response

        // When
        val result = repository.searchFood("test", 1, 10)

        // Then
        assertThat(result.isFailure).isTrue()
        coVerify { api.searchFood("test", 1, 10) }
    }

    @Test
    fun `searchFood returns failure when API call throws exception`() = runBlocking {
        // Given
        coEvery { api.searchFood(any(), any(), any()) } throws Exception("Exception")

        // When
        val result = repository.searchFood("test", 1, 10)

        // Then
        assertThat(result.isFailure).isTrue()
    }
}