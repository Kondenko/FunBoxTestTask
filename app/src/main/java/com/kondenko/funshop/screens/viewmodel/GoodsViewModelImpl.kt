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
    getGoods: GetGoods,
    val addGood: AddGood,
    val buyGood: BuyGood
) : ViewModel(), BuyerViewModel, AdminViewModel {

    private val state = MutableLiveData<State<List<Good>>>()

    private val disposables = CompositeDisposable()

    init {
        state.value = Loading.Goods
        disposables += getGoods(null)
            .subscribeBy(
                onNext = {
                    val newState = when {
                        it.isEmpty() -> Empty
                        state.value is Loading.Purchase -> Success.ItemBought(it)
                        else -> Success.ItemsFetched(it)
                    }
                    setState(newState)
                },
                onError = { setErrorState(it, state.value) }
            )
    }

    override fun state(): LiveData<State<List<Good>>> = state

    override fun invoke(action: Action.Buyer) {
        val currentState = state.value
        disposables += when (action) {
            is Action.Buyer.Buy -> {
                buyGood(action.good)
                    .doOnSubscribe {
                        currentState?.data?.let { currentData ->
                            val itemMetadata = action.good.metadata?.copy(isBeingProcessed = true)
                            val processedItem = action.good.copy(metadata = itemMetadata)
                            val loadingState = Loading.Purchase(currentData.replace(processedItem) {
                                it.id == processedItem.id
                            })
                            setState(loadingState)
                        }
                    }
                    .subscribeBy(onError = { setErrorState(it, currentState) })
            }
        }
    }

    override fun invoke(action: Action.Admin) {
        val currentState = state.value
        when (action) {
            is Action.Admin.Create -> {
                disposables += addGood(action.good).subscribeBy(
                    onComplete = { state.value = Success.ItemAdded(listOf(action.good)) },
                    onError = { setErrorState(it, currentState) }
                )
            }
            else -> return
        }
    }

    private fun setState(state: State<List<Good>>) {
        this.state.value = state
    }

    private fun setErrorState(throwable: Throwable, currentState: State<List<Good>>?) = setState(
        if (currentState is Success) Error(throwable, currentState.data)
        else Error(throwable)
    )

}

