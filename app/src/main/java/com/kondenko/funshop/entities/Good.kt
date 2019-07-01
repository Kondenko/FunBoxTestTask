package com.kondenko.funshop.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Good(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val name: String,
        val price: Double,
        val quantity: Long,
        @Ignore
        val metadata: Metadata?
) : Parcelable {

    sealed class Payload {
        data class Name(val old: String, val new: String) : Payload()
        data class Price(val old: Double, val new: Double) : Payload()
        data class Quantity(val old: Long, val new: Long) : Payload()
    }

    // For Room
    constructor(id: Int? = null, name: String, price: Double, quantity: Long) :
            this(id, name, price, quantity, null)

    fun getPayloads(other: Good?): List<Payload> {
        if (this == other || other == null) return emptyList()
        val payloads = mutableListOf<Payload>()
        if (name != other.name) payloads += Payload.Name(name, other.name)
        if (price != other.price) payloads += Payload.Price(price, other.price)
        if (quantity != other.quantity) payloads += Payload.Quantity(quantity, other.quantity)
        return payloads
    }

}

@Parcelize
data class Metadata(
        val displayPrice: String,
        val displayQuantity: String,
        val isBeingProcessed: Boolean
) : Parcelable