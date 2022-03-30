package com.mdr.food.data.food.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomFood (
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val imageUrl: String?,
    val minPrice: Int
)