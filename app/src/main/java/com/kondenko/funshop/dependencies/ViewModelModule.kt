package com.kondenko.funshop.dependencies

import com.kondenko.funshop.data.GoodsRepository
import com.kondenko.funshop.data.StringFormatter
import com.kondenko.funshop.domain.AddOrUpdateGood
import com.kondenko.funshop.domain.BuyGood
import com.kondenko.funshop.domain.GetGoods
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule : ModuleCreator {
    override fun create() = module {
        single { StringFormatter(androidContext()) }
        single { GoodsRepository(get(), get()) }
        single { AddOrUpdateGood(get(), get()) }
        single { BuyGood(get(), get()) }
        single { GetGoods(get(), get()) }
        viewModel { GoodsViewModelImpl(get(), get(), get()) }
    }
}