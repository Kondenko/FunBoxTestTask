package com.kondenko.funshop.domain

import com.kondenko.funshop.data.AppGoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.SchedulerContainer

class AddGood(private val goodsRepository: AppGoodsRepository, schedulersContainer: SchedulerContainer) :
    UseCase.Do<Good>(schedulersContainer) {

    override fun build(params: Good?) = goodsRepository.add(params)
}