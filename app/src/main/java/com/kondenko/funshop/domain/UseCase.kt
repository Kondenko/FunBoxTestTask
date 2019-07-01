package com.kondenko.funshop.domain

import com.kondenko.funshop.utils.Schedulers
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

sealed class UseCase<T, R, C> {

    protected abstract fun build(params: T): C

    abstract operator fun invoke(params: T): C

    abstract class Fetch<T, R>(private val schedulers: Schedulers) : UseCase<T, R, Single<R>>() {

        override fun invoke(params: T): Single<R> =
            build(params)
                .subscribeOn(schedulers.worker)
                .observeOn(schedulers.ui)

    }

    abstract class Subscribe<T, R>(private val schedulers: Schedulers) :
        UseCase<T, R, Observable<R>>() {

        override fun invoke(params: T): Observable<R> =
            build(params)
                .subscribeOn(schedulers.worker)
                .observeOn(schedulers.ui)

    }

    abstract class Do<T>(private val schedulers: Schedulers) : UseCase<T, Nothing, Completable>() {

        override fun invoke(params: T): Completable =
            build(params)
                .subscribeOn(schedulers.worker)
                .observeOn(schedulers.ui)

    }

}
