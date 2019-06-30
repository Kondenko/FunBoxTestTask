package com.kondenko.funshop.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Good(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val price: Double,
    val quantity: Long,
    @Ignore
    val metadata: Metadata?
) {
    // For Room
    constructor(id: Int? = null, name: String, price: Double, quantity: Long) :
            this(id, name, price, quantity, null)
}

data class Metadata(
    val displayPrice: String,
    val displayQuantity: String,
    val isBeingProcessed: Boolean
)