package com.kondenko.funshop.dependencies

import com.kondenko.funshop.data.CsvGoodsProvider
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
        single { CsvGoodsProvider(androidContext()) }
        single { StringFormatter(androidContext()) }
        single { GoodsRepository(goodsDao = get(), initialGoodsProvider = get<CsvGoodsProvider>(), stringFormatter = get()) }
        single { AddOrUpdateGood(goodsRepository = get(), schedulers = get()) }
        single { BuyGood(goodsRepository = get(), schedulers = get()) }
        single { GetGoods(goodsRepository = get(), schedulers = get()) }
        viewModel { GoodsViewModelImpl(getGoods = get(), addOrUpdateGood = get(), buyGood = get()) }
    }
}