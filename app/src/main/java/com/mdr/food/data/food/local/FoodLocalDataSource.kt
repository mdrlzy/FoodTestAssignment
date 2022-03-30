package com.mdr.food.data.food.local

import com.mdr.food.domain.Food

class FoodLocalDataSource(private val foodDao: FoodDao) {
    suspend fun insert(food: List<Food>) {
        foodDao.insert(food.map { it.toRoom() })
    }

    suspend fun provide(category: String) = foodDao.provide(category).map {
        it.toFood()
    }

    private fun Food.toRoom() =
        RoomFood(id, title, description, category, imageUrl, minPrice)

    private fun RoomFood.toFood() =
        Food(id, title, description, category, imageUrl, minPrice)
}