package com.kondenko.funshop.presentation.main

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kondenko.funshop.R
import com.kondenko.funshop.presentation.backend.FragmentBackend
import com.kondenko.funshop.presentation.store.FragmentStore
import com.kondenko.funshop.presentation.viewmodel.GoodsViewModelImpl
import com.kondenko.funshop.utils.animate
import com.kondenko.funshop.utils.scale
import com.kondenko.funshop.utils.transaction
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class ActivityMain : AppCompatActivity() {

    @Suppress("unused") // This ViewModel is shared with fragments
    private val goodsViewModel by viewModel<GoodsViewModelImpl>()

    private val tagStore = "storeFront"
    private val fragmentStore = FragmentStore()

    private val tagBackend = "backend"
    private val fragmentBackend = FragmentBackend()

    private val keySelectedTag = "selectedTag"
    private var selectedTag: String? = null

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
        selectedTag = tag
        val duration: Long = 100
        framelayout_container.animateOut(duration) {
            supportFragmentManager.transaction {
                if (!supportFragmentManager.isStateSaved) replace(R.id.framelayout_container, fragment, tag)
                it.animateIn(duration)
            }
        }
    }

    private fun View.animateOut(duration: Long, endAction: (View) -> Unit) = animate {
        this.duration = duration
        interpolator = AccelerateInterpolator()
        scale(0.99f)
        alpha(0f)
        withEndAction { endAction(this@animateOut) }
    }

    private fun View.animateIn(duration: Long) = animate {
        this.duration = duration
        interpolator = DecelerateInterpolator(2f)
        scale(1f)
        alpha(1f)
    }

    override fun onBackPressed() {
        val shouldPopBackStack = fragmentBackend.run {
            host != null && isShowingEditor
        }
        if (shouldPopBackStack) fragmentBackend.goBack()
        else super.onBackPressed()
    }

}
