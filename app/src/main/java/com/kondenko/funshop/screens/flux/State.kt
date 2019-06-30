package com.kondenko.funshop.screens.flux

sealed class State<out T>(open val data: T?) {

    sealed class Success<T>(override val data: T) : State<T>(data) {
        data class ItemsFetched<T>(override val data: T) : Success<T>(data)
        data class ItemAdded<T>(override val data: T) : Success<T>(data)
        data class ItemBought<T>(override val data: T) : Success<T>(data)
    }

    sealed class Loading<T>(override val data: T?) : State<T>(data) {
        object Goods : Loading<Nothing>(null)
        data class Purchase<T>(override val data: T) : Loading<T>(data)
    }

    data class Error<T>(val throwable: Throwable, override val data: T? = null) : State<T>(data)

    object Empty : State<Nothing>(null)

}