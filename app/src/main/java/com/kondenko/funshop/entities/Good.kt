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
    val displayPrice: String? = null,
    @Ignore
    val displayQuantity: String? = null
) {

    // For room
    constructor(
        id: Int? = null,
        name: String,
        price: Double,
        quantity: Long
    ) : this(id, name, price, quantity, null, null)

}