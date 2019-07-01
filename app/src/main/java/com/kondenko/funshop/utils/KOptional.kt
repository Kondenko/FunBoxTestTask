package com.kondenko.funshop.utils

class KOptional<T> private constructor(val value: T?) {

    companion object {
        fun empty() = KOptional(null)
        fun <T> of(value: T) = KOptional(value)
        fun <T> ofNullable(value: T?) = KOptional(value)
    }

    fun isEmpty() = value == null

    fun isNotEmpty() = value != null

}