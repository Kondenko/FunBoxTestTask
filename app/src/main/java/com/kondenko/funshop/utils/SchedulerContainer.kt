package com.kondenko.funshop.utils

import io.reactivex.Scheduler

data class SchedulerContainer(val workerScheduler: Scheduler, val uiScheduler: Scheduler)