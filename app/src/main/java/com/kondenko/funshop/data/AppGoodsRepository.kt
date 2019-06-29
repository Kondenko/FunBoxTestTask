package com.kondenko.funshop.data

import com.kondenko.funshop.entities.Good

class AppGoodsRepository(private val goodsDao: GoodsDao) {

    fun getGoods() = goodsDao.getGoods()

    fun add(good: Good) = goodsDao.insert(good)

    fun update(good: Good) = goodsDao.insert(good)

}