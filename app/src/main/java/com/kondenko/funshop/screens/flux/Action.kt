package com.kondenko.funshop.screens.flux

import com.kondenko.funshop.entities.Good

sealed class Action {

    sealed class Buyer : Action() {
        object GetGoods : Buyer()
        data class Buy(val good: Good) : Buyer()
    }

    sealed class Admin : Action() {
        object GetGoods : Admin()
        data class Create(val good: Good) : Admin()
        data class Update(val good: Good) : Admin()
        data class Delete(val good: Good) : Admin()
    }

}