package com.kondenko.funshop.data

import com.kondenko.funshop.entities.Good
import io.reactivex.Observable

class AppGoodsRepository(private val goodsDao: GoodsDao) {

    fun getGoods(): Observable<Good> = goodsDao.getGoods()

    fun add(good: Good) = goodsDao.insert(good)

    fun update(good: Good) = goodsDao.insert(good)

}