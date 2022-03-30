package com.mdr.food.data.food.remote

import com.mdr.food.data.FoodApi
import com.mdr.food.data.ResponseMovie
import com.mdr.food.domain.Food

class FoodRemoteDataSource(
    val api: FoodApi
) {
    suspend fun search(category: String, page: Int = 1): List<Food> {
        return api.searchRepositories(category, page).results.map {
            it.toFood(category)
        }
    }

    private fun ResponseMovie.toFood(category: String) =
        Food(id, title, overview, category, resolvePosterUrl(), 350)
}