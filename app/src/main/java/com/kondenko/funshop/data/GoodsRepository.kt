package com.kondenko.funshop.data

import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.entities.Metadata
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class GoodsRepository(
    private val goodsDao: GoodsDao,
    private val initialGoodsProvider: GoodsProvider,
    private val stringFormatter: StringFormatter
) {

    fun getGoods() = goodsDao.getGoods()
        .map { it.addMetadata() }

    private fun List<Good>.addMetadata() = map {
        it.copy(
            metadata = Metadata(
                displayPrice = stringFormatter.formatPrice(it.price),
                displayQuantity = stringFormatter.formatQuantity(it.quantity),
                isBeingProcessed = false
            )
        )
    }

    fun addOrUpdate(good: Good) = good.run {
        if (id == null) goodsDao.insert(this)
        else goodsDao.update(this)
    }

    fun update(good: Good) = goodsDao.insert(good)

    fun prepopulateDatabase() =
        initialGoodsProvider
            .getGoods()
            .flatMap { it.toObservable() }
            .flatMapCompletable { goodsDao.insert(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { Timber.d("Database populated") },
                onError = { Timber.e(it, "Error populating the database") }
            )

}