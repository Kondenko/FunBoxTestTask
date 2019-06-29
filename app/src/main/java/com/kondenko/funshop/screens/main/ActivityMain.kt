package com.kondenko.funshop.screens.main

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kondenko.funshop.R
import com.kondenko.funshop.screens.backend.FragmentBackend
import com.kondenko.funshop.screens.store.FragmentStore
import com.kondenko.funshop.screens.viewmodel.GoodsViewModel
import com.kondenko.funshop.utils.animate
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class ActivityMain : AppCompatActivity() {

    private val tagStore = "storeFront"
    private val fragmentStore = FragmentStore()

    private val tagBackend = "backend"
    private val fragmentBackend = FragmentBackend()

    private val keySelectedTag = "selectedTag"
    private var selectedTag: String? = null

    private val goodsViewModel by viewModel<GoodsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectedTag = savedInstanceState?.getString(keySelectedTag)
        selectStore()
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
        fab_action.apply {
            setImageResource(R.drawable.ic_add_to_cart)
            playPopAnimation()
        }
    }

    private fun selectBackend() {
        setFragment(fragmentBackend, tagBackend)
        fab_action.apply {
            setImageResource(R.drawable.ic_add)
            playPopAnimation()
        }
    }

    private fun View.playPopAnimation() {
        val originalScaleX = scaleX
        val originalScaleY = scaleY
        val factor = 0.85f
        val animDuration = 150L
        animate {
            duration = animDuration
            interpolator = AccelerateInterpolator()
            scaleX(originalScaleX * factor)
            scaleY(originalScaleY * factor)
            withEndAction {
                animate {
                    duration = animDuration
                    interpolator = DecelerateInterpolator()
                    scaleX(originalScaleX)
                    scaleY(originalScaleY)
                }
            }
        }
    }

    private fun setFragment(fragment: Fragment, tag: String) {
        if (selectedTag == null || selectedTag != tag) {
            selectedTag = tag
            with(supportFragmentManager.beginTransaction()) {
                replace(R.id.framelayout_container, fragment, tag)
                commit()
            }
        }
    }

}
