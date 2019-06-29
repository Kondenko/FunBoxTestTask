package com.kondenko.funshop.dependencies

import com.kondenko.funshop.utils.SchedulerContainer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

class RxModule : ModuleCreator {
    override fun create() = module {
        factory {
            SchedulerContainer(
                workerScheduler = Schedulers.io(),
                uiScheduler = AndroidSchedulers.mainThread()
            )
        }
    }
}