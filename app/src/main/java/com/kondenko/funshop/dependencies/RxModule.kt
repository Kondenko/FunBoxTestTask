package com.kondenko.funshop.dependencies

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

class RxModule : ModuleCreator {
    override fun create() = module {
        factory {
            com.kondenko.funshop.utils.Schedulers(
                    worker = Schedulers.io(),
                    ui = AndroidSchedulers.mainThread()
            )
        }
    }
}