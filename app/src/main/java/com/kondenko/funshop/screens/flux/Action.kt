package com.kondenko.funshop.screens.flux

import com.kondenko.funshop.entities.Good

sealed class Action {

    sealed class Buyer : Action() {
        data class Buy(val good: Good) : Buyer()
        object CleanUpLastBoughtItem : Buyer()
    }

    sealed class Admin : Action() {
        data class ShowGoodEditScreen(val good: Good?) : Admin()
        object HideGoodEditScreen : Admin()
        object GoBack : Admin()
        data class Create(val good: Good) : Admin()
    }

}