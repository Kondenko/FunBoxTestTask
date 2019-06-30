package com.kondenko.funshop.screens

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import com.kondenko.funshop.utils.subscribe

abstract class FragmentGoods : Fragment() {

    abstract var adapterGoods: AdapterGoods

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel().state().subscribe(viewLifecycleOwner, ::onStateChanged)
    }

    abstract fun viewModel(): GoodsViewModelImpl

    abstract fun onStateChanged(state: State<List<Good>>)

    @CallSuper
    protected open fun updateData(data: List<Good>) {
        adapterGoods.items = data
    }


}