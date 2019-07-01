package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.Schedulers
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class GetGoods(private val goodsRepository: GoodsRepository, private val schedulers: Schedulers) :
        UseCase.Subscribe<Nothing?, List<Good>>(schedulers) {

    private val delaySec: Long = 5

    override fun build(params: Nothing?) = Completable.timer(delaySec, TimeUnit.SECONDS, schedulers.ui).andThen(
            goodsRepository.getGoods()
    )

}