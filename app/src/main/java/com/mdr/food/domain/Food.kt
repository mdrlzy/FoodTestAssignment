package com.mdr.food.domain

data class Food(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val imageUrl: String?,
    val minPrice: Int
)