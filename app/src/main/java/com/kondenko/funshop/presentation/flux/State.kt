package com.kondenko.funshop.presentation.flux

sealed class State<out T>(open val data: List<T>?) {

    sealed class Success<T>(override val data: List<T>) : State<T>(data) {
        data class ItemsFetched<T>(override val data: List<T>) : Success<T>(data)
        data class ItemBought<T>(override val data: List<T>) : Success<T>(data)
    }

    sealed class Loading<T>(override val data: List<T>?) : State<T>(data) {
        object Goods : Loading<Nothing>(null)
        data class Purchase<T>(override val data: List<T>) : Loading<T>(data)
        data class Mutation<T>(override val data: List<T>?) : Loading<T>(data)
    }

    data class Error<T>(val throwable: Throwable, override val data: List<T>? = null) : State<T>(data)

    /**
     * Indicates the data on the screen is being edited
     */
    data class Mutation<out T>(val item: T?, override val data: List<T>?, val y: Int, val height: Float) : State<T>(data)

    data class MutationFinished<T>(override val data: List<T>?, val y: Int, val height: Float) : State<T>(data)

    object Empty : State<Nothing>(null)

    object GoBack : State<Nothing>(null)

}