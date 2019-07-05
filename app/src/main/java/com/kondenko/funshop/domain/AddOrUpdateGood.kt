package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.Schedulers
import java.util.concurrent.TimeUnit

class AddOrUpdateGood(private val goodsRepository: GoodsRepository, private val schedulers: Schedulers) :
    UseCase.Do<Good>(schedulers) {

    private val delaySec: Long = 5

    override fun build(params: Good) = goodsRepository.addOrUpdate(params)
        .delay(delaySec, TimeUnit.SECONDS, schedulers.ui)

}