package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.Schedulers
import io.reactivex.Observable

class GetGoods(private val goodsRepository: GoodsRepository, schedulers: Schedulers) :
    UseCase.Subscribe<Nothing?, List<Good>>(schedulers) {

    override fun build(params: Nothing?): Observable<List<Good>> =
        goodsRepository.getGoods()

}