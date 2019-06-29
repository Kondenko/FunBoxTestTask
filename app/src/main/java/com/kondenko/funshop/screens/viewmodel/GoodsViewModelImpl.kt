package com.kondenko.funshop.screens.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kondenko.funshop.domain.AddGood
import com.kondenko.funshop.domain.GetGoods
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable

class GoodsViewModelImpl(
    val getGoods: GetGoods,
    val addGood: AddGood
) : ViewModel(), BuyerViewModel, AdminViewModel {

    private val state = MutableLiveData<State<List<Good>>>()

    private val disposables = CompositeDisposable()

    init {
        state.value = State.Loading
    }

    override fun state(): LiveData<State<List<Good>>> = state

    override fun invoke(action: Action.Buyer) {
        when (action) {
            Action.Buyer.GetGoods -> {
                disposables += getGoods()
                    .flatMap { it.toObservable() }
                    .filter { it.quantity > 0 }
                    .toList()
                    .subscribeBy(
                        onSuccess = { toState(it) },
                        onError = { toState(it) }
                    )
            }
            else -> return
        }
    }

    override fun invoke(action: Action.Admin) {
        when (action) {
            is Action.Admin.GetGoods -> {
                disposables += getGoods().subscribeBy(
                    onNext = { toState(it) },
                    onError = { toState(it) }
                )
            }
            is Action.Admin.Create -> {
                disposables += addGood(action.good).subscribeBy(
                    onComplete = { state.value = State.Success.ItemAdded(listOf(action.good)) },
                    onError = ::toState
                )
            }
            else -> return
        }
    }

    private fun toState(data: List<Good>) {
        state.value = State.Success.ItemsFetched(data)
    }

    private fun toState(throwable: Throwable) {
        state.value = State.Error(throwable)
    }

}

