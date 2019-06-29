package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.FragmentGoods
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.AdminViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModel
import kotlinx.android.synthetic.main.fragment_backend.view.*
import kotlinx.android.synthetic.main.item_backend_good.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.DecimalFormat

class FragmentBackend : FragmentGoods() {

    private val vm: AdminViewModel by sharedViewModel<GoodsViewModel>()

    override lateinit var adapterGoods: AdapterGoods

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_backend, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterGoods = AdapterGoods(view.context, R.layout.item_backend_good) { itemView, item ->
            with(itemView) {
                itemAdminTextviewName.text = item.name
                itemAdminTextviewPrice.text = DecimalFormat("0.#").format(item.price).toString()
                itemAdminTextviewQuantity.text = context.getString(R.string.backend_quantity, item.quantity)
            }
        }
        with(view.backendRecyclerViewGoods) {
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            this.adapter = adapterGoods
        }
        vm(Action.Admin.GetGoods)
    }

    override fun viewModel(): GoodsViewModel = vm as GoodsViewModel

    override fun onStateChanged(state: State<List<Good>>) = when (state) {
        is State.Success -> state.render()
        else -> Unit
    }

}