package com.kondenko.funshop.screens.viewmodel

import androidx.lifecycle.LiveData
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State

interface AdminViewModel {

    fun state(): LiveData<State<List<Good>>>

    operator fun invoke(action: Action.Admin)

}