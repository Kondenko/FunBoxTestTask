package com.kondenko.funshop.domain

import com.kondenko.funshop.data.AppGoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.SchedulerContainer
import io.reactivex.Observable

class GetGoods(private val goodsRepository: AppGoodsRepository, schedulersContainer: SchedulerContainer) :
    UseCase.Subscribe<Nothing, Good>(schedulersContainer) {

    override fun build(params: Nothing?): Observable<Good> = goodsRepository.getGoods()

}