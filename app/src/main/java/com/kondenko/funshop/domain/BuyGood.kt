package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.Schedulers
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class BuyGood(private val goodsRepository: GoodsRepository, private val schedulers: Schedulers) :
    UseCase.Do<Good>(schedulers) {

    private val delay: Long = 3

    override fun build(params: Good) =
        if (params.quantity > 0) {
            val delay = Completable.timer(delay, TimeUnit.SECONDS, schedulers.worker)
            val purchase = goodsRepository.update(params.copy(quantity = params.quantity - 1))
            Completable.concatArray(delay, purchase)
        } else {
            Completable.error(IllegalArgumentException("Can't buy a good with quantity = 0: $params"))
        }

}