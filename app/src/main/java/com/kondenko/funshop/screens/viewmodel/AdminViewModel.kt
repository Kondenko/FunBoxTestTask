package com.kondenko.funshop.screens.viewmodel

import com.kondenko.funshop.screens.flux.Action

interface AdminViewModel : GoodsViewModel {

    operator fun invoke(action: Action.Admin)

}