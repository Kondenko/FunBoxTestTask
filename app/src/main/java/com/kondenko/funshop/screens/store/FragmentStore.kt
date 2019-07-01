package com.kondenko.funshop.screens.store

import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnPreDraw
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.AdapterGoods
import com.kondenko.funshop.screens.FragmentGoods
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.BuyerViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import com.kondenko.funshop.utils.animate
import kotlinx.android.synthetic.main.fragment_store.view.*
import kotlinx.android.synthetic.main.item_store_front_good.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.hypot

class FragmentStore : FragmentGoods() {

    private val viewModel: BuyerViewModel by viewModel<GoodsViewModelImpl>()

    override val fragmentLayout: Int = R.layout.fragment_store

    override val itemLayout: Int = R.layout.item_store_front_good

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.storeFrontViewPager.adapter = adapterGoods
    }

    override fun viewModel(): GoodsViewModelImpl = viewModel as GoodsViewModelImpl

    override fun onStateChanged(state: State<List<Good>>) {
        view?.storeFrontProgressBar?.isVisible = state is State.Loading.Goods
        when (state) {
            is State.Success.ItemBought -> adapterGoods.items = state.data
            is State.Success -> updateData(state.data)
            is State.Loading.Purchase -> adapterGoods.items = state.data
            is Error -> state.data?.let(::updateData)
            else -> return
        }
    }

    override fun updateData(data: List<Good>) {
        super.updateData(data.filter { it.quantity > 0 })
    }

    override fun bindItem(view: View, item: Good, payloads: List<Any>) = with(view) {
        val isBeingProcessed = item.metadata?.isBeingProcessed == true
        itemStoreFrontTextviewName.text = item.name
        itemStoreFrontTextviewPrice.text = item.metadata?.displayPrice
        itemStoreFrontTextviewQuantity.text = item.metadata?.displayQuantity
        itemStoreFrontProgressBar?.isVisible = isBeingProcessed
        itemStoreFrontButtonBuy?.isEnabled = !isBeingProcessed
        itemStoreFrontButtonBuy.setOnClickListener {
            viewModel(Action.Buyer.Buy(item))
        }
        if (payloads.contains(AdapterGoods.Payload.PurchaseCompleted)) {
            itemStoreFrontImageViewDone.animateCircularReveal {
                fadeOut {
                    viewModel(Action.Buyer.CleanUpLastBoughtItem)
                }
            }
        }
    }

    private fun View.animateCircularReveal(onEnd: View.() -> Unit) {
        isVisible = true
        alpha = 1f
        doOnPreDraw {
            val cx = width / 2
            val cy = bottom
            val startRadius = 0f
            val endRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            with(ViewAnimationUtils.createCircularReveal(this, cx, cy, startRadius, endRadius)) {
                duration = 300L
                interpolator = DecelerateInterpolator()
                doOnEnd { this@animateCircularReveal.onEnd() }
                start()
            }
        }
    }

    private fun View.fadeOut(onEnd: () -> Unit) = animate {
        startDelay = 1000L
        duration = 150L
        interpolator = AccelerateInterpolator()
        withEndAction {
            isInvisible = true
            onEnd()
        }
        alpha(0f)
    }

}