package com.kondenko.funshop.screens.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.AdapterGoods
import com.kondenko.funshop.screens.FragmentGoods
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.BuyerViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import kotlinx.android.synthetic.main.fragment_store.view.*
import kotlinx.android.synthetic.main.item_store_front_good.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FragmentStore : FragmentGoods() {

    private val viewModel: BuyerViewModel by viewModel<GoodsViewModelImpl>()

    override lateinit var adapterGoods: AdapterGoods

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_store, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterGoods =
            AdapterGoods(view.context, R.layout.item_store_front_good) { itemView, item ->
                with(itemView) {
                    val isBeingProcessed = item.metadata?.isBeingProcessed == true
                    itemStoreFrontTextviewName.text = item.name
                    itemStoreFrontTextviewPrice.text = item.metadata?.displayPrice
                    itemStoreFrontTextviewQuantity.text = item.metadata?.displayQuantity
                    itemStoreFrontProgressBar?.isVisible = isBeingProcessed
                    itemStoreFrontButtonBuy?.isEnabled = !isBeingProcessed
                    itemStoreFrontButtonBuy.setOnClickListener {
                        viewModel(Action.Buyer.Buy(item))
                    }
                }
            }
        view.store_front_viewpager.run {
            adapter = adapterGoods
        }
    }

    override fun viewModel(): GoodsViewModelImpl = viewModel as GoodsViewModelImpl

    override fun onStateChanged(state: State<List<Good>>) {
        when (state) {
            is State.Success -> updateData(state.data)
            is State.Loading.Purchase -> updateData(state.data)
            is Error -> state.data?.let(::updateData)
            else -> return
        }.also { Timber.d("Store state updated: $state") }
    }

    override fun updateData(data: List<Good>) {
        super.updateData(data.filter { it.quantity > 0 })
    }
}