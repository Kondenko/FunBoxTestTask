package com.kondenko.funshop.utils

import io.reactivex.Scheduler

data class Schedulers(val worker: Scheduler, val ui: Scheduler)