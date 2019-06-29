package com.kondenko.funshop.dependencies

import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.viewmodel.GoodsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModuleCreator : ModuleCreator {
    override fun create() = module {
        viewModel { GoodsViewModel() }
    }
}