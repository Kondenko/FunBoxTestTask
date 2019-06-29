package com.kondenko.funshop.domain

import com.kondenko.funshop.data.AppGoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.SchedulerContainer

class GetGoods(private val goodsRepository: AppGoodsRepository, schedulersContainer: SchedulerContainer) :
    UseCase.Subscribe<Nothing, List<Good>>(schedulersContainer) {

    override fun build(params: Nothing?) = goodsRepository.getGoods()

}