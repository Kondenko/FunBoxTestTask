package com.kondenko.funshop.presentation.flux

import com.kondenko.funshop.entities.Good

sealed class Action {

    sealed class Buyer : Action() {
        data class Buy(val good: Good) : Buyer()
        object CleanUpLastBoughtItem : Buyer()
    }

    sealed class Admin : Action() {
        data class ShowGoodEditScreen(val good: Good?, val y: Int, val height: Float) : Admin()
        object HideGoodEditScreen : Admin()
        object GoBack : Admin()
        data class EditOrCreate(val good: Good) : Admin()
    }

}