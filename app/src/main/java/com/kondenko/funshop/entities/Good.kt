package com.kondenko.funshop.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Good(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val price: Double,
    val quantity: Long
)