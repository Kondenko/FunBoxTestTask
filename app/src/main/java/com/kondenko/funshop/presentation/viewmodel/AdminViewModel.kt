package com.kondenko.funshop.presentation.viewmodel

import com.kondenko.funshop.presentation.flux.Action

interface AdminViewModel : GoodsViewModel {

    operator fun invoke(action: Action.Admin)

}