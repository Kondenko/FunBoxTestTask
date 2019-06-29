package com.kondenko.funshop.dependencies

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.domain.AddGood
import com.kondenko.funshop.domain.GetGoods
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule : ModuleCreator {
    override fun create() = module {
        single { GoodsRepository(get()) }
        single { AddGood(get(), get() ) }
        single { GetGoods(get(), get() ) }
        viewModel { GoodsViewModelImpl(get(), get()) }
    }
}