package com.kondenko.funshop.screens.backend

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.recyclerview.BaseAdapter
import kotlinx.android.synthetic.main.item_backend_good.view.*
import java.text.DecimalFormat

class AdapterBackendGoods(context: Context) : BaseAdapter<Good, AdapterBackendGoods.GoodViewHolder>(context) {

    private val priceFormat = DecimalFormat("0.#")

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GoodViewHolder(inflate(R.layout.item_backend_good, parent))

    override fun getItemId(position: Int): Long = items[position].id?.toLong() ?: RecyclerView.NO_ID

    inner class GoodViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(item: Good) = with(itemView) {
            itemAdminTextviewName.text = item.name
            itemAdminTextviewPrice.text = priceFormat.format(item.price).toString()
            itemAdminTextviewQuantity.text = context.getString(R.string.backend_quantity, item.quantity)
        }
    }

}