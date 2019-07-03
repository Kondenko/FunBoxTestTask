package com.kondenko.funshop.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.screens.flux.State
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import com.kondenko.funshop.utils.subscribe
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

abstract class FragmentGoods : Fragment() {

    protected val disposables: CompositeDisposable by inject()

    protected lateinit var adapterGoods: AdapterGoods

    protected abstract val fragmentLayout: Int

    protected abstract val itemLayout: Int

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(fragmentLayout, container, false)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterGoods = AdapterGoods(view.context, itemLayout, ::bindItem)
        viewModel().state().subscribe(viewLifecycleOwner, ::onStateChanged)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    protected abstract fun viewModel(): GoodsViewModelImpl

    protected abstract fun onStateChanged(state: State<Good>)

    protected abstract fun bindItem(view: View, item: Good, payloads: List<Any>)

    @CallSuper
    protected open fun updateData(data: List<Good>) {
        adapterGoods.items = data
    }

}