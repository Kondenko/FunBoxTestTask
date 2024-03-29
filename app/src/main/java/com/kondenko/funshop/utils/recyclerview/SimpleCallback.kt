package com.kondenko.funshop.utils.recyclerview

import androidx.recyclerview.widget.DiffUtil

class SimpleCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val areItemsTheSame: ((oldItem: T, newItem: T) -> Boolean) = { old, new ->
        old == new
    },
    private val areContentsTheSame: ((oldItem: T, newItem: T) -> Boolean) = { old, new ->
        old == new
    },
    private val getChangePayload: ((oldItem: T, newItem: T) -> Any?)? = null
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return getChangePayload?.invoke(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

}