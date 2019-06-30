package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.SchedulerContainer
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class BuyGood(private val goodsRepository: GoodsRepository, private val schedulersContainer: SchedulerContainer) :
    UseCase.Fetch<Good, Good>(schedulersContainer) {

    private val delay: Long = 3

    override fun build(params: Good) =
        if (params.quantity > 0) {
            Single.timer(delay, TimeUnit.SECONDS, schedulersContainer.workerScheduler).flatMap {
                goodsRepository
                    .update(params.copy(quantity = params.quantity - 1))
                    .andThen(Single.just(params.copy(metadata = params.metadata?.copy(isBeingProcessed = false))))
            }
        } else {
            Single.error(IllegalArgumentException("Can't buy a good with quantity = 0: $params"))
        }

}