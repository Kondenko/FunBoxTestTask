package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.SchedulerContainer

class GetGoods(private val goodsRepository: GoodsRepository, schedulersContainer: SchedulerContainer) :
    UseCase.Subscribe<Nothing?, List<Good>>(schedulersContainer) {

    override fun build(params: Nothing?) = goodsRepository.getGoods()

}