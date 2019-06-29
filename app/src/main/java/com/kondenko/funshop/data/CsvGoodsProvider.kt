package com.kondenko.funshop.data

import android.content.Context
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.allSubgroups
import com.kondenko.funshop.utils.parseLines
import io.reactivex.Observable
import java.util.regex.Pattern

class CsvGoodsProvider(private val context: Context) : GoodsProvider {

    private val fileName = "data.csv"

    private val csvLineRegex = "\"(.*?)\""

    override fun getGoods() =
        Observable.fromPublisher<String> { context.assets.open(fileName).parseLines(it) }
            .map { it.parseGood() }
            .toList()
            .toObservable()

    private fun String.parseGood(): Good {
        val matches = Pattern.compile(csvLineRegex).matcher(this).allSubgroups().values.toList()
        val name = matches[0]
        val price = matches[1]?.toDoubleOrNull()
        val quantity = matches[2]?.toLongOrNull()
        if (matches.size != 3) throw IllegalArgumentException("Could't parse the string as a good: $this")
        if (name == null) throw IllegalArgumentException("Name not found in $this")
        if (price == null) throw IllegalArgumentException("Price not found in $this")
        if (quantity == null) throw IllegalArgumentException("Quantity not found in $this")
        return Good(null, name, price, quantity)
    }

}