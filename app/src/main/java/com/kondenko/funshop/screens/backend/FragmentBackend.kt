package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.view.clicks
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.FragmentGoods
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.AdminViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import com.kondenko.funshop.utils.transaction
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_backend.view.*
import kotlinx.android.synthetic.main.item_backend_good.view.*
import kotlinx.android.synthetic.main.layout_backend_list.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class FragmentBackend : FragmentGoods() {

    private val viewModel: AdminViewModel by sharedViewModel<GoodsViewModelImpl>()

    override val fragmentLayout: Int = R.layout.fragment_backend

    override val itemLayout: Int = R.layout.item_backend_good

    private val fragmentItemEditor = FragmentItemEditor()

    private val editorTag = "editor"

    var isShowingEditor = true
        private set

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.backendRecyclerViewGoods) {
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            this.adapter = adapterGoods
        }
        disposables += view.backendFabNewGood.clicks().subscribeBy(Timber::e) {
            viewModel(Action.Admin.ShowGoodEditScreen(null))
        }
        disposables += fragmentItemEditor.cancelClicks().subscribeBy(Timber::e) {
            viewModel(Action.Admin.HideGoodEditScreen)
        }
        disposables += fragmentItemEditor.saveClicks().subscribeBy(Timber::e) {
            Toast.makeText(view.context, "Saving ${it.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun viewModel(): GoodsViewModelImpl = viewModel as GoodsViewModelImpl

    override fun onStateChanged(state: State<Good>) {
        Timber.d("Backend state updated: $state")
        view?.backendProgressBar?.isVisible = state is State.Loading.Goods
        when (state) {
            is State.Success.ItemsFetched<Good> -> {
                hideGoodEditor()
                updateData(state.data)
            }
            is State.Loading.Goods -> {
                hideGoodEditor()
            }
            is State.Mutation<Good> -> {
                state.data?.let(::updateData)
                showGoodEditor(state.item)
            }
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
            viewModel(Action.Admin.ShowGoodEditScreen(item))
        }
    }

    private fun showGoodEditor(good: Good?) {
        isShowingEditor = true
        fragmentItemEditor.setGood(good)
        childFragmentManager.transaction {
            if (childFragmentManager.findFragmentByTag(editorTag) == null) {
                add(R.id.backendFrameLayoutContainer, fragmentItemEditor, editorTag)
            }
            show(fragmentItemEditor)
        }
        fragmentItemEditor.setGood(good)
        view?.backendLayoutList?.isGone = true
    }

    private fun hideGoodEditor() {
        isShowingEditor = false
        childFragmentManager.transaction {
            hide(fragmentItemEditor)
        }
        view?.backendLayoutList?.isVisible = true
    }

}