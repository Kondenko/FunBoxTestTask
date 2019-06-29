package com.kondenko.funshop.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.backend.AdapterGoods
import com.kondenko.funshop.screens.flux.Action
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.GoodsViewModel
import com.kondenko.funshop.utils.subscribe

abstract class FragmentGoods : Fragment() {

    abstract var adapterGoods: AdapterGoods

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_backend, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel().invoke(Action.Admin.GetGoods)
        viewModel().state().subscribe(viewLifecycleOwner, ::onStateChanged)
    }

    abstract fun viewModel(): GoodsViewModel

    abstract fun onStateChanged(state: State<List<Good>>)

    protected fun State.Success<List<Good>>.render() {
        adapterGoods.items = this.data
    }

}