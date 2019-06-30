package com.kondenko.funshop.screens.flux

import com.kondenko.funshop.entities.Good

sealed class State<out T> {
    sealed class Success<T> : State<T>() {
        data class ItemsFetched<T>(val data: T) : Success<T>()
        data class ItemAdded<T>(val data: T?): Success<T>()
        data class ItemBought<T>(val data: T?): Success<T>()
    }
    data class Error(val throwable: Throwable) : State<Nothing>()
    object Empty : State<Nothing>()
    sealed class Loading : State<Nothing>() {
        object Goods : Loading()
        data class Purchase(val good: Good) : Loading()
    }
}