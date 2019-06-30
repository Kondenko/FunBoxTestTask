package com.kondenko.funshop.screens.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kondenko.funshop.domain.AddGood
import com.kondenko.funshop.domain.BuyGood
import com.kondenko.funshop.domain.GetGoods
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.flux.State.*
import com.kondenko.funshop.utils.replace
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class GoodsViewModelImpl(
    val getGoods: GetGoods,
    val addGood: AddGood,
    val buyGood: BuyGood
) : ViewModel(), BuyerViewModel, AdminViewModel {

    private val state = MutableLiveData<State<List<Good>>>()

    private val disposables = CompositeDisposable()

    init {
        state.value = State.Loading.Goods
    }

    override fun state(): LiveData<State<List<Good>>> = state

    override fun invoke(action: Action.Buyer) {
        disposables += when (action) {
            is Action.Buyer.GetGoods -> {
                getGoods(null)
                    .map { it.filter { it.quantity > 0 } }
                    .subscribeBy(
                        onSuccess = {
                            setState(it)
                        },
                        onError = { setState(it) }
                    )
            }
            is Action.Buyer.Buy -> {
                buyGood(action.good)
                    .doOnSubscribe { setState(State.Loading.Purchase(action.good)) }
                    .subscribeBy(
                        onComplete = { setState(State.Success.ItemBought(listOf(action.good))) },
                        onError = { setState(it) }
                    )
            }
        }
    }

    override fun invoke(action: Action.Admin) {
        when (action) {
            is Action.Admin.GetGoods -> {
                disposables += getGoods(null).subscribeBy(
                    onNext = { setState(it) },
                    onError = { setState(it) }
                )
            }
            is Action.Admin.Create -> {
                disposables += addGood(action.good).subscribeBy(
                    onComplete = { state.value = State.Success.ItemAdded(listOf(action.good)) },
                    onError = ::setState
                )
            }
            else -> return
        }
    }

    private fun setState(state: State<List<Good>>) {
        this.state.value = state
    }

    private fun setState(goods: List<Good>) = setState(
        if (goods.isEmpty()) State.Empty
        else State.Success.ItemsFetched(goods)
    )

    private fun setState(throwable: Throwable) = setState(State.Error(throwable))

}

