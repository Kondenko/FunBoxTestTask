package com.kondenko.funshop.screens.viewmodel

import androidx.lifecycle.LiveData
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.flux.State

interface GoodsViewModel {

    fun state(): LiveData<State<Good>>

}