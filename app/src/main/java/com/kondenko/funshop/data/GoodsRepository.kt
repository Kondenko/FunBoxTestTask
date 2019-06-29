package com.kondenko.funshop.data

import com.kondenko.funshop.entities.Good
import io.reactivex.Completable

class GoodsRepository(
    private val goodsDao: GoodsDao,
    private val stringFormatter: StringFormatter
) {

    fun getGoods() = goodsDao.getGoods()
        .map {
            it.map {
                it.copy(
                    displayPrice = stringFormatter.formatPrice(it.price),
                    displayQuantity = stringFormatter.formatQuantity(it.quantity)
                )
            }
        }

    fun add(good: Good?) =
        good?.let { goodsDao.insert(good) }
            ?: Completable.error(NullPointerException("Can't add a null value to the database"))

    fun update(good: Good) = goodsDao.insert(good)

}