package com.kondenko.funshop.screens.flux

sealed class State<out T> {
    sealed class Success<T> : State<T>() {
        data class ItemsFetched<T>(val data: T) : Success<T>()
        data class ItemAdded<T>(val data: T?): Success<T>()
    }
    data class Error(val throwable: Throwable) : State<Nothing>()
    object Empty : State<Nothing>()
    object Loading : State<Nothing>()
}