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
import com.kondenko.funshop.utils.peekOrNull
import com.kondenko.funshop.utils.replace
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import java.util.*

class GoodsViewModelImpl(
        getGoods: GetGoods,
        val addGood: AddGood,
        val buyGood: BuyGood
) : ViewModel(), BuyerViewModel, AdminViewModel {

    private val state = MutableLiveData<State<Good>>()

    private val initialState = Loading.Goods

    private val distinctStateHistory = Stack<State<Good>>()

    private val disposables = CompositeDisposable()

    init {
        state.value = initialState
        disposables += getGoods(null)
                .map {
                    val currentState = state.value
                    when {
                        it.isEmpty() -> Empty
                        currentState is Loading.Purchase -> Success.ItemBought(it)
                        currentState is Mutation -> {
                            val itemBeingEdited = it.find { it.id == currentState.item?.id }
                            currentState.copy(item = itemBeingEdited, data = it)
                        }
                        else -> Success.ItemsFetched(it)
                    }
                }
                .subscribeBy(
                        onNext = { setState(it) },
                        onError = { setErrorState(it, state.value) }
                )
    }

    override fun state(): LiveData<State<Good>> = state

    override fun invoke(action: Action.Admin) {
        val currentState = state.value
        when (action) {
            is Action.Admin.Create -> {
                disposables += addGood(action.good).subscribeBy(
                        onComplete = { invoke(Action.Admin.HideGoodEditScreen) },
                        onError = { setErrorState(it, currentState) }
                )
            }
            is Action.Admin.ShowGoodEditScreen -> {
                setState(Mutation(action.good, currentState?.data))
            }
            is Action.Admin.HideGoodEditScreen -> {
                distinctStateHistory.pop()
                setState(distinctStateHistory.peekOrNull() ?: initialState)
            }
            is Action.Admin.GoBack -> {
                if (state.value is Mutation) invoke(Action.Admin.HideGoodEditScreen)
                else setState(GoBackDefault)
            }
        }
    }

    override fun invoke(action: Action.Buyer) {
        val currentState = state.value
        when (action) {
            is Action.Buyer.Buy -> {
                disposables += buyGood(action.good)
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
            is Action.Buyer.CleanUpLastBoughtItem -> {
                setState(Success.ItemsFetched(currentState!!.data!!))
            }
        }
    }

    private fun setState(state: State<Good>) {
        this.state.value = state
        if (distinctStateHistory.isEmpty() || state::class != distinctStateHistory.peek()::class) {
            distinctStateHistory.push(state)
        }
    }

    private fun setErrorState(throwable: Throwable, currentState: State<Good>?) = setState(
            if (currentState is Success) Error(throwable, currentState.data)
            else Error(throwable)
    )

}

