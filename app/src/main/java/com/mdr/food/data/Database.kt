package com.mdr.food.data

import androidx.room.RoomDatabase
import com.mdr.food.data.food.local.FoodDao
import com.mdr.food.data.food.local.RoomFood

@androidx.room.Database(
    entities = [
        RoomFood::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        const val DB_NAME = "Food.db"
    }
}