package com.kondenko.funshop.screens

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.recyclerview.BaseAdapter
import com.kondenko.funshop.utils.recyclerview.SimpleCallback

class AdapterGoods(
        context: Context,
        private val layoutRes: Int,
        private val binder: (View, Good, List<Any>) -> Unit
) : BaseAdapter<Good, AdapterGoods.GoodViewHolder>(context) {

    sealed class Payload {
        object PurchaseCompleted : Payload()
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GoodViewHolder(inflate(layoutRes, parent))

    override fun onBindViewHolder(holder: GoodViewHolder, position: Int) = Unit // no-op, we need a payload

    override fun onBindViewHolder(holder: GoodViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        holder.bind(items[position], payloads)
    }

    override fun getItemId(position: Int): Long = items[position].id?.toLong() ?: RecyclerView.NO_ID

    override fun getDiffCallback(oldList: List<Good>, newList: List<Good>) = SimpleCallback(
            oldList,
            newList,
            areItemsTheSame = { a, b -> a.id == b.id },
            areContentsTheSame = { a, b -> a == b },
            getChangePayload = ::getChangePayload
    )

    private fun getChangePayload(old: Good, new: Good): Any? = when {
        old.metadata?.isBeingProcessed == true && new.metadata?.isBeingProcessed == false -> {
            Payload.PurchaseCompleted
        }
        else -> null
    }

    inner class GoodViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(item: Good, payload: List<Any>) {
            binder(itemView, item, payload)
        }
    }

}