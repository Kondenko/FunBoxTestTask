package com.kondenko.funshop.screens.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.view.doOnPreDraw
import androidx.core.view.isGone
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
import timber.log.Timber
import kotlin.math.hypot

class FragmentStore : FragmentGoods() {

    private val viewModel: BuyerViewModel by viewModel<GoodsViewModelImpl>()

    override lateinit var adapterGoods: AdapterGoods

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_store, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterGoods =
                AdapterGoods(view.context, R.layout.item_store_front_good) { itemView, item, payloads ->
                    with(itemView) {
                        val isBeingProcessed = item.metadata?.isBeingProcessed == true
                        itemStoreFrontTextviewName.text = item.name
                        itemStoreFrontTextviewPrice.text = item.metadata?.displayPrice
                        itemStoreFrontTextviewQuantity.text = item.metadata?.displayQuantity
                        itemStoreFrontButtonBuy.setOnClickListener {
                            viewModel(Action.Buyer.Buy(item))
                        }
                        if (payloads.contains(AdapterGoods.Payload.PurchaseCompleted)) {
                            itemStoreFrontImageViewDone.animateCircularReveal {
                                it.fadeOut {
                                    viewModel(Action.Buyer.CleanUpLastBoughtItem)
                                }
                            }
                        }
                        itemStoreFrontProgressBar?.isVisible = isBeingProcessed
                        itemStoreFrontButtonBuy?.isEnabled = !isBeingProcessed
                    }
                }
        view.store_front_viewpager.run {
            adapter = adapterGoods
        }
    }

    override fun viewModel(): GoodsViewModelImpl = viewModel as GoodsViewModelImpl

    override fun onStateChanged(state: State<List<Good>>) {
        when (state) {
            is State.Success.ItemBought -> adapterGoods.items = state.data
            is State.Success -> updateData(state.data)
            is State.Loading.Purchase -> adapterGoods.items = state.data
            is Error -> state.data?.let(::updateData)
            else -> return
        }.also { Timber.d("Store state updated: $state") }
    }

    override fun updateData(data: List<Good>) {
        super.updateData(data.filter { it.quantity > 0 })
    }

    private fun View.animateCircularReveal(onEnd: (View) -> Unit) {
        isVisible = true
        doOnPreDraw {
            val cx = width / 2
            val cy = bottom
            val startRadius = 0f
            val endRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            ViewAnimationUtils.createCircularReveal(this, cx, cy, startRadius, endRadius).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                addListener(onEnd = { onEnd(this@animateCircularReveal) }, onCancel = { Timber.w("Reveal cancelled")})
                start()
            }
        }
    }

    private fun View.fadeOut(onEnd: () -> Unit) = animate {
        startDelay = 1000L
        duration = 150L
        interpolator = AccelerateInterpolator()
        withEndAction { isGone = true }
        withEndAction(onEnd)
        alpha(0f)
    }

}