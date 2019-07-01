package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.Schedulers

class AddGood(private val goodsRepository: GoodsRepository, schedulers: Schedulers) :
    UseCase.Do<Good>(schedulers) {

    override fun build(params: Good) = goodsRepository.add(params)

}