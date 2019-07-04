package com.kondenko.funshop.screens.backend

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ScrollView
import androidx.core.view.children
import androidx.core.view.isVisible
import com.kondenko.funshop.utils.animatedValue

class RevealView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ScrollView(context, attrs, defStyleAttr, defStyleRes) {

    private val topTravelDuration: Long = 200

    private val clipHeightDuration = topTravelDuration + 100

    private val contentAlphaDuration: Long = topTravelDuration

    private val inInterpolator = DecelerateInterpolator()
    private val outInterpolator = AccelerateInterpolator()

    private var clipHeight: Float = 0f

    private var shapeAnimators: List<ValueAnimator>? = null

    private val originalTop: Int by lazy { top }

    fun reveal(initialHeight: Float, initialTop: Float, content: ViewGroup) {
        post {
            shapeAnimators = listOf(
                createClipHeightAnimator(initialHeight),
                createViewTopAnimator(initialTop),
                createContentAlphaAnimator(content)
            ).apply { forEach(Animator::start) }
        }
    }

    fun hide(onFinished: () -> Unit) {
        shapeAnimators?.forEach {
            it.interpolator = outInterpolator
            it.reverse()
        }
        // Trigger the onFinished callback after all animations have completed
        // without introducing concurrency
        postDelayed(onFinished, shapeAnimators?.map { it.duration }?.min() ?: 0L)
        clipHeight = 0f
        top = originalTop
        isVisible = true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.clipRect(0f, 0f, width.toFloat(), clipHeight)
        super.onDraw(canvas)
    }

    private fun createClipHeightAnimator(initialHeight: Float) =
        ValueAnimator.ofFloat(initialHeight, bottom.toFloat()).apply {
            duration = clipHeightDuration
            interpolator = inInterpolator
            addUpdateListener {
                clipHeight = it.animatedValue()
                invalidate()
            }
        }

    private fun createViewTopAnimator(initialTop: Float) =
        ValueAnimator.ofInt(initialTop.toInt(), originalTop).apply {
            duration = topTravelDuration
            interpolator = inInterpolator
            addUpdateListener {
                top = it.animatedValue()
            }
        }

    private fun createContentAlphaAnimator(contentView: ViewGroup) =
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = contentAlphaDuration
            interpolator = inInterpolator
            addUpdateListener { animator ->
                contentView.children.forEach {
                    it.alpha = animator.animatedValue()
                }
            }
        }

}