package com.kondenko.funshop.screens.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kondenko.funshop.domain.GetGoods
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import io.reactivex.rxkotlin.subscribeBy

class GoodsViewModel(val getGoods: GetGoods) : ViewModel(), BuyerViewModel, AdminViewModel {

    private val state = MutableLiveData<State>()

    init {
        state.value = State.Loading
    }

    override fun state(): LiveData<State> = state

    override fun invoke(action: Action.Buyer) {
        when (action) {
            Action.Buyer.GetGoods -> {
                getGoods()
                    .filter { it.quantity > 0 }
                    .map { State.Success(it) }
                    .subscribeBy(
                        onNext = { state.value = it },
                        onError = { toState(it) }
                    )
            }
            else -> return
        }
    }

    override fun invoke(action: Action.Admin) {
        when (action) {
            Action.Admin.GetGoods -> {
                getGoods().subscribeBy(
                    onNext = { toState(it) },
                    onError = { toState(it) }
                )
            }
            else -> return
        }
    }

    private fun <T> toState(data: T) {
        state.value = State.Success(data)
    }

    private fun toState(throwable: Throwable) {
        state.value = State.Error(throwable)
    }

}

interface BuyerViewModel {

    fun state(): LiveData<State>

    operator fun invoke(action: Action.Buyer)

}

interface AdminViewModel {

    fun state(): LiveData<State>

    operator fun invoke(action: Action.Admin)

}