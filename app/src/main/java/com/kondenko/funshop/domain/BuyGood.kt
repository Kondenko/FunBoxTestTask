package com.kondenko.funshop.domain

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.SchedulerContainer
import io.reactivex.Completable

class BuyGood(private val goodsRepository: GoodsRepository, schedulersContainer: SchedulerContainer) :
    UseCase.Do<Good>(schedulersContainer) {

    override fun build(params: Good) =
        if (params.quantity > 0) goodsRepository.update(params.copy(quantity = params.quantity - 1))
        else Completable.error(IllegalArgumentException("Can't buy a good with quantity = 0: $params"))

}