package com.kondenko.funshop.dependencies

import org.koin.core.module.Module

fun getModules(): List<Module> = listOf(
    ViewModelModule,
    PersistenceModule(),
    RxModule()
).map(ModuleCreator::create)