package com.kondenko.funshop.screens.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.FragmentGoods
import com.kondenko.funshop.screens.backend.AdapterGoods
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
        adapterGoods = AdapterGoods(view.context, R.layout.item_store_front_good) { itemView, item ->
            with(itemView) {
                itemStoreFrontTextviewName.text = item.name
                itemStoreFrontTextviewPrice.text = item.displayPrice
                itemStoreFrontTextviewQuantity.text = item.displayQuantity
                itemStoreFrontButtonBuy.setOnClickListener {
                    viewModel(Action.Buyer.Buy(item))
                }
            }
        }
        view.store_front_viewpager.run {
            adapter = adapterGoods
        }
        viewModel(Action.Buyer.GetGoods)
    }

    override fun viewModel(): GoodsViewModelImpl = viewModel as GoodsViewModelImpl

    override fun onStateChanged(state: State<List<Good>>) = when (state) {
        is State.Success.ItemsFetched -> state.render()
        is State.Success.ItemBought, is State.Error -> playPurchaseAnimation(false)
        is State.Loading.Purchase -> playPurchaseAnimation(true)
        else -> Unit
    }.also { Timber.d("Store state updated: $state") }

    private fun playPurchaseAnimation(play: Boolean) {
        view?.apply {
            itemStoreFrontProgressBar?.isVisible = play
            itemStoreFrontButtonBuy?.isEnabled = !play
        }
    }

}