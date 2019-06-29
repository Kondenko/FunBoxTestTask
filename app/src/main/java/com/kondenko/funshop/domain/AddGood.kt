package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.SchedulerContainer

class AddGood(private val goodsRepository: GoodsRepository, schedulersContainer: SchedulerContainer) :
    UseCase.Do<Good>(schedulersContainer) {

    override fun build(params: Good?) = goodsRepository.add(params)

}