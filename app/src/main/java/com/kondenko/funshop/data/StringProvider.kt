package com.kondenko.funshop.data

import android.content.Context
import com.kondenko.funshop.R
import java.text.DecimalFormat

class StringFormatter(private val context: Context) {

    fun formatQuantity(value: Long): String {
        return context.getString(R.string.item_good_quantity, value)
    }

    fun formatPrice(value: Double) : String {
        return context.getString(R.string.item_good_price, DecimalFormat("0.#").format(value))
    }

}