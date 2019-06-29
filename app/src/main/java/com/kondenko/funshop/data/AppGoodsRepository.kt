package com.kondenko.funshop.data

import com.kondenko.funshop.entities.Good
import io.reactivex.Completable

class AppGoodsRepository(private val goodsDao: GoodsDao) {

    fun getGoods() = goodsDao.getGoods()

    fun add(good: Good?) =
        good?.let { goodsDao.insert(good) }
            ?: Completable.error(NullPointerException("Can't add a null value to the database"))

    fun update(good: Good) = goodsDao.insert(good)

}