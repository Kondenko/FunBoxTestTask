package com.kondenko.funshop.dependencies

import com.kondenko.funshop.data.AppGoodsRepository
import com.kondenko.funshop.domain.GetGoods
import com.kondenko.funshop.screens.viewmodel.GoodsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule : ModuleCreator {
    override fun create() = module {
        single { AppGoodsRepository(get()) }
        single { GetGoods(get(), get() ) }
        viewModel { GoodsViewModel(get()) }
    }
}