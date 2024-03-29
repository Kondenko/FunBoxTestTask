package com.kondenko.funshop

import android.app.Application
import com.kondenko.funshop.dependencies.getModules
import com.kondenko.funshop.utils.TimberLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@App)
            modules(getModules())
            logger(TimberLogger())
        }
    }

}