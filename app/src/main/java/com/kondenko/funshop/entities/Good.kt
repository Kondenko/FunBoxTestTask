package com.kondenko.funshop.entities

import androidx.room.PrimaryKey
import androidx.room.Query

data class Good(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val price: Int,
    val quantity: Int
)