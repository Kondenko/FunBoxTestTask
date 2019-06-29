package com.kondenko.funshop.data

import com.kondenko.funshop.entities.Good
import io.reactivex.Observable

interface GoodsProvider {

    fun getGoods(): Observable<Good>

}