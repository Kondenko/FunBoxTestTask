package com.kondenko.funshop.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kondenko.funshop.R
import com.kondenko.funshop.screens.backend.FragmentBackend
import com.kondenko.funshop.screens.store.FragmentStore
import com.kondenko.funshop.screens.viewmodel.GoodsViewModelImpl
import com.kondenko.funshop.utils.transaction
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class ActivityMain : AppCompatActivity() {

    private val tagStore = "storeFront"
    private val fragmentStore = FragmentStore()

    private val tagBackend = "backend"
    private val fragmentBackend = FragmentBackend()

    private val keySelectedTag = "selectedTag"
    private var selectedTag: String? = null

    private val goodsViewModel by viewModel<GoodsViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectedTag = savedInstanceState?.getString(keySelectedTag)
        if (selectedTag == tagBackend) selectBackend()
        else selectStore()
        bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_store -> selectStore()
                R.id.navigation_backend -> selectBackend()
            }
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(keySelectedTag, selectedTag)
        super.onSaveInstanceState(outState)
    }

    private fun selectStore() {
        setFragment(fragmentStore, tagStore)
    }

    private fun selectBackend() {
        setFragment(fragmentBackend, tagBackend)
    }

    private fun setFragment(fragment: Fragment, tag: String) {
        if (selectedTag == null || selectedTag != tag) {
            selectedTag = tag
            supportFragmentManager.transaction {
                replace(R.id.framelayout_container, fragment, tag)
            }
        }
    }

    override fun onBackPressed() {
        val shouldPopBackStack = fragmentBackend.run {
            host != null && childFragmentManager.backStackEntryCount > 0
        }
        if (shouldPopBackStack) fragmentBackend.goBack()
        else super.onBackPressed()
    }

}
