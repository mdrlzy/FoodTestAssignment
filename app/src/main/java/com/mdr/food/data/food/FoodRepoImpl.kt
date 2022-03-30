package com.mdr.food.data.food

import com.mdr.food.data.food.local.FoodLocalDataSource
import com.mdr.food.data.food.remote.FoodRemoteDataSource
import com.mdr.food.domain.Food
import com.mdr.food.domain.FoodRepo
import com.mdr.food.domain.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodRepoImpl(
    private val remoteDataSource: FoodRemoteDataSource,
    private val localDataSource: FoodLocalDataSource,
    private val networkStatus: NetworkStatus
) : FoodRepo {
    override suspend fun getFoodByCategory(category: String): List<Food> =
        withContext(Dispatchers.IO) {
            if (networkStatus.isOnline()) {
                val food = remoteDataSource.search(category)
                launch { localDataSource.insert(food) }
                return@withContext food
            } else {
                return@withContext localDataSource.provide(category)
            }
        }

    override fun getCategories(): List<String> = listOf(
        "Pizza",
        "Combo",
        "Dessert",
        "Drinks"
    )
}