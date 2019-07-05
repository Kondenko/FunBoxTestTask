package com.kondenko.funshop.screens.backend

import android.animation.TimeInterpolator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.view.clicks
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.FragmentGoods
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.flux.State.*
import com.kondenko.funshop.screens.viewmodel.AdminViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import com.kondenko.funshop.utils.animate
import com.kondenko.funshop.utils.showError
import com.kondenko.funshop.utils.transaction
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.item_backend_good.view.*
import kotlinx.android.synthetic.main.layout_backend_list.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import kotlin.math.roundToInt

class FragmentBackend : FragmentGoods() {

    private val viewModel: AdminViewModel by sharedViewModel<GoodsViewModelImpl>()

    override val fragmentLayout: Int = R.layout.fragment_backend

    override val itemLayout: Int = R.layout.item_backend_good

    private val fragmentItemEditor = FragmentItemEditor()

    private val editorTag = "editor"

    var isShowingEditor = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.backendRecyclerViewGoods) {
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            this.adapter = adapterGoods
        }
        disposables += view.backendFabNewGood.clicks().subscribeBy(Timber::e) {
            viewModel(Action.Admin.ShowGoodEditScreen(null, 0, 0f))
        }
        disposables += fragmentItemEditor.cancelClicks().subscribeBy(Timber::e) {
            viewModel(Action.Admin.HideGoodEditScreen)
        }
        disposables += fragmentItemEditor.saveClicks().subscribeBy(Timber::e) {
            viewModel(Action.Admin.EditOrCreate(it))
        }
    }

    override fun viewModel(): GoodsViewModelImpl = viewModel as GoodsViewModelImpl

    override fun onStateChanged(state: State<Good>) {
        state.data?.let(::updateData)
        view?.backendProgressBar?.isVisible = state is Loading.Goods
        when (state) {
            is Success.ItemsFetched<Good> -> hideGoodEditor()
            is Mutation -> showGoodEditor(state.item, state.y, state.height)
            is MutationFinished -> hideGoodEditor(state.y, state.height)
            is Error -> context?.showError(state.throwable)
            is Loading.Goods -> hideGoodEditor()
        }
    }

    fun goBack() {
        viewModel(Action.Admin.GoBack)
    }

    override fun bindItem(view: View, item: Good, payloads: List<Any>) = with(view) {
        itemAdminTextviewName.text = item.name
        itemAdminTextviewPrice.text = item.metadata?.displayPrice
        itemAdminTextviewQuantity.text = item.metadata?.displayQuantity
        view.setOnClickListener {
            viewModel(Action.Admin.ShowGoodEditScreen(item, view.y.roundToInt(), view.height.toFloat()))
        }
    }

    private fun showGoodEditor(good: Good?, y: Int, height: Float) {
        childFragmentManager.transaction {
            if (childFragmentManager.findFragmentByTag(editorTag) == null) {
                add(R.id.backendFrameLayoutContainer, fragmentItemEditor, editorTag)
            }
            show(fragmentItemEditor)
            if (!isShowingEditor) {
                runOnCommit {
                    animateOverlay(true)
                    fragmentItemEditor.reveal(y, height)
                }
            }
            isShowingEditor = true
            fragmentItemEditor.setGood(good)
        }
    }

    private fun hideGoodEditor(y: Int = 0, height: Float = 0f) {
        animateOverlay(false)
        fragmentItemEditor.hide(y, height) {
            childFragmentManager.transaction {
                hide(fragmentItemEditor)
                isShowingEditor = false
            }
        }
    }

    private fun animateOverlay(show: Boolean) = view?.backendListCover?.animate {
        duration = 200L
        interpolator = if (show) DecelerateInterpolator() else AccelerateInterpolator() as TimeInterpolator
        alpha(if (show) 1f else 0f)
        withLayer()
    }

}