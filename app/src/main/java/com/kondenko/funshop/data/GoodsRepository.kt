package com.kondenko.funshop.data

import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.entities.Metadata

class GoodsRepository(
    private val goodsDao: GoodsDao,
    private val stringFormatter: StringFormatter
) {

    fun getGoods() = goodsDao.getGoods()
        .map {
            it.map {
                it.copy(
                    metadata = Metadata(
                        displayPrice = stringFormatter.formatPrice(it.price),
                        displayQuantity = stringFormatter.formatQuantity(it.quantity),
                        isBeingProcessed = false
                    )
                )
            }
        }

    fun addOrUpdate(good: Good) = good.run {
        if (id == null) goodsDao.insert(this)
        else goodsDao.update(this)
    }

    fun update(good: Good) = goodsDao.insert(good)

}