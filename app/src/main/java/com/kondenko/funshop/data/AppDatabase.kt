package com.kondenko.funshop.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kondenko.funshop.entities.Good

@Database(entities = [Good::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goodsDao(): GoodsDao
}