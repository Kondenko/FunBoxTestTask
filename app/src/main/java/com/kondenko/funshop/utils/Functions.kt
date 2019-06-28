package com.kondenko.funshop.utils

import android.view.View
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.scale(value: Float) = scaleXBy(value).scaleYBy(value)

fun View.animate(animation: ViewPropertyAnimator.() -> ViewPropertyAnimator) {
    animate().animation().start()
}