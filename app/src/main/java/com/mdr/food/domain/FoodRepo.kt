package com.mdr.food.domain

interface FoodRepo {
    suspend fun getFoodByCategory(category: String): List<Food>
    fun getCategories(): List<String>
}