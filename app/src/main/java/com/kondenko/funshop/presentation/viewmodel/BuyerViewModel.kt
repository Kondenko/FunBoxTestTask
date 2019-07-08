package com.kondenko.funshop.presentation.viewmodel

import com.kondenko.funshop.presentation.flux.Action

interface BuyerViewModel : GoodsViewModel {

    operator fun invoke(action: Action.Buyer)

}