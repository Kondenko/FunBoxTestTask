package com.kondenko.funshop.utils

data class KOptional<T>(val value: T?) {
    fun isEmpty() = value == null
    fun isNotEmpty() = value != null
}