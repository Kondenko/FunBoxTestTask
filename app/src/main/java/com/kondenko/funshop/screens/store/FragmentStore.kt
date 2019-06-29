package com.kondenko.funshop.screens.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kondenko.funshop.R
import com.kondenko.funshop.screens.viewmodel.BuyerViewModel
import com.kondenko.funshop.screens.viewmodel.GoodsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentStore : Fragment() {

    private val vm: BuyerViewModel by viewModel<GoodsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_store, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}