package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.Schedulers
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class BuyGood(private val goodsRepository: GoodsRepository, private val schedulers: Schedulers) :
    UseCase.Fetch<Good, Good>(schedulers) {

    private val delay: Long = 3

    override fun build(params: Good) =
        if (params.quantity > 0) {
            Single.timer(delay, TimeUnit.SECONDS, schedulers.worker).flatMap {
                goodsRepository
                    .update(params.copy(quantity = params.quantity - 1))
                    .andThen(Single.just(params.copy(metadata = params.metadata?.copy(isBeingProcessed = false))))
            }
        } else {
            Single.error(IllegalArgumentException("Can't buy a good with quantity = 0: $params"))
        }

}