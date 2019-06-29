package com.kondenko.funshop.screens.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State

class GoodsViewModel: ViewModel(), BuyerViewModel, AdminViewModel {

    private val state = MutableLiveData<State>()

    init {
        state.value = State.Loading
    }

    fun state(): LiveData<State> = state

    override fun <T : Action.Buyer> invoke(action: T) {

    }

    override fun <T : Action.Admin> invoke(action: T) {

    }

}

interface BuyerViewModel {
    operator fun <T : Action.Buyer> invoke(action: T)
}

interface AdminViewModel {
    operator fun <T : Action.Admin> invoke(action: T)
}