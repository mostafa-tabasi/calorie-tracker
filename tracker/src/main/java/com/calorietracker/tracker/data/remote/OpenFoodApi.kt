package com.calorietracker.tracker.data.remote

import com.calorietracker.tracker.data.remote.dto.SearchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodApi {
    companion object {
        const val BASE_URL = "https://us.openfoodfacts.org/"
    }

    @GET("cgi/search.pl?search_simple=1&json=1&action=process&fields=product_name,nutriments,image_front_thumb_url")
    suspend fun searchFood(
        @Query("search_terms") query: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): Response<SearchDto>
}