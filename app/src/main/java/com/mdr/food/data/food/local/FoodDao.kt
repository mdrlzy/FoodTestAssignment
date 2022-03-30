package com.mdr.food.data.food.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(food: List<RoomFood>)

    @Query("SELECT * FROM RoomFood WHERE category = :category")
    fun provide(category: String): List<RoomFood>
}