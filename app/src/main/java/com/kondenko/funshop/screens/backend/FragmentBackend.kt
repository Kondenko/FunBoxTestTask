package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.FragmentGoods
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.AdminViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import kotlinx.android.synthetic.main.fragment_backend.view.*
import kotlinx.android.synthetic.main.item_backend_good.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class FragmentBackend : FragmentGoods() {

    private val vm: AdminViewModel by sharedViewModel<GoodsViewModelImpl>()

    override val fragmentLayout: Int = R.layout.fragment_backend

    override val itemLayout: Int = R.layout.item_backend_good

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.backendRecyclerViewGoods) {
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            this.adapter = adapterGoods
        }
    }

    override fun viewModel(): GoodsViewModelImpl = vm as GoodsViewModelImpl

    override fun onStateChanged(state: State<List<Good>>) {
        view?.backendProgressBar?.isVisible = state is State.Loading.Goods
        when (state) {
            is State.Success.ItemsFetched<List<Good>> -> updateData(state.data)
            else -> Unit
        }.also { Timber.d("Backend state updated: $state") }
    }

    override fun bindItem(view: View, item: Good, payloads: List<Any>) = with(view) {
        itemAdminTextviewName.text = item.name
        itemAdminTextviewPrice.text = item.metadata?.displayPrice
        itemAdminTextviewQuantity.text = item.metadata?.displayQuantity
    }

}