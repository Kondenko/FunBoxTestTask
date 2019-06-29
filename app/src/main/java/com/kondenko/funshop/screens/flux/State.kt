package com.kondenko.funshop.screens.flux

sealed class State {
    data class Success<T>(val data: T) : State()
    data class Error(val throwable: Throwable) : State()
    object Empty : State()
    object Loading : State()
}