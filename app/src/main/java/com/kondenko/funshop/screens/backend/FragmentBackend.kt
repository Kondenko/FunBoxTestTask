package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.AdapterGoods
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

    override lateinit var adapterGoods: AdapterGoods

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_backend, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterGoods =
            AdapterGoods(view.context, R.layout.item_backend_good) { itemView, item, _ ->
                with(itemView) {
                    itemAdminTextviewName.text = item.name
                    itemAdminTextviewPrice.text = item.metadata?.displayPrice
                    itemAdminTextviewQuantity.text = item.metadata?.displayQuantity
                }
            }
        with(view.backendRecyclerViewGoods) {
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            this.adapter = adapterGoods
        }
    }

    override fun viewModel(): GoodsViewModelImpl = vm as GoodsViewModelImpl

    override fun onStateChanged(state: State<List<Good>>) = when (state) {
        is State.Success.ItemsFetched<List<Good>> -> updateData(state.data)
        else -> Unit
    }.also { Timber.d("Backend state updated: $state") }

}