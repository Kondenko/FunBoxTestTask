package com.kondenko.funshop.screens.flux

sealed class State<out T> {
    data class Success<T>(val data: T) : State<T>()
    data class Error(val throwable: Throwable) : State<Nothing>()
    object Empty : State<Nothing>()
    object Loading : State<Nothing>()
}