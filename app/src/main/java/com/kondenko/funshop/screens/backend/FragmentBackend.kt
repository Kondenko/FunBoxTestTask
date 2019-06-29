package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.AdminViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModel
import com.kondenko.funshop.utils.subscribe
import kotlinx.android.synthetic.main.fragment_backend.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class FragmentBackend : Fragment() {

    private val vm: AdminViewModel by sharedViewModel<GoodsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_backend, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.invoke(Action.Admin.GetGoods)
        val adapter = AdapterBackendGoods(view.context)
        with(view.backendRecyclerViewGoods) {
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            this.adapter = adapter
        }
        vm.state().subscribe(viewLifecycleOwner) {
            Timber.d("backend state updated: $it")
            when (it) {
                is State.Success<List<Good>> -> it.render(adapter)
            }
        }
    }

    private fun State.Success<List<Good>>.render(adapter: AdapterBackendGoods) {
        adapter.items = this.data
    }

}