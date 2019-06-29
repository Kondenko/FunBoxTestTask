package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kondenko.funshop.R
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.viewmodel.AdminViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModel
import com.kondenko.funshop.utils.subscribe
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class FragmentBackend : Fragment() {

    private val vm: AdminViewModel by sharedViewModel<GoodsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_backend, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.state().subscribe(viewLifecycleOwner) {
            Timber.d("backend state updated: $it")
        }
        vm.invoke(Action.Admin.GetGoods)
    }

}