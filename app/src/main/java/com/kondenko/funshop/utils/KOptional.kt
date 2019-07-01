package com.kondenko.funshop.utils

class KOptional<T>(val value: T?) {

    fun isEmpty() = value == null

    fun isNotEmpty() = value != null

}