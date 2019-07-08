package com.kondenko.funshop.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.presentation.flux.State

interface GoodsViewModel {

    fun state(): LiveData<State<Good>>

}