package com.kondenko.funshop.dependencies

import org.koin.core.module.Module

interface ModuleCreator {
    fun create(): Module
}