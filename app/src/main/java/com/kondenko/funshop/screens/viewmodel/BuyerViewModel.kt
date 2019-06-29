package com.kondenko.funshop.screens.viewmodel

import com.kondenko.funshop.screens.flux.Action

interface BuyerViewModel : GoodsViewModel {

    operator fun invoke(action: Action.Buyer)

}