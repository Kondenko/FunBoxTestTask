package com.kondenko.funshop.screens.backend

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ScrollView
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.kondenko.funshop.utils.animate
import com.kondenko.funshop.utils.animatedValue

class RevealScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ScrollView(context, attrs, defStyleAttr, defStyleRes) {

    private val topTravelDuration: Long = 200
    private val clipHeightDuration = topTravelDuration + 100
    private val contentAlphaDuration: Long = topTravelDuration

    private val inInterpolator = DecelerateInterpolator()
    private val outInterpolator = DecelerateInterpolator()

    private var clipHeightAnimator: ValueAnimator? = null
    private var viewTopAnimator: ValueAnimator? = null
    private var contentAlphaAnimator: ValueAnimator? = null

    private var clipHeight: Float? = null

    private val originalTop: Int by lazy { top }

    fun reveal(initialHeight: Float, initialTop: Int, content: ViewGroup) {
        post {
            clipHeightAnimator = createClipHeightAnimator(initialHeight).also(ValueAnimator::start)
            viewTopAnimator = createViewTopAnimator(initialTop).also(ValueAnimator::start)
            contentAlphaAnimator = createContentAlphaAnimator(content).also(ValueAnimator::start)
        }
        clipHeight = null
        alpha = 1f
        top = originalTop
    }

    fun hide(initialHeight: Float, initialTop: Int, content: ViewGroup, onFinished: () -> Unit) {
        var isReversed = true
        if (clipHeightAnimator == null || viewTopAnimator == null || contentAlphaAnimator == null) {
            clipHeightAnimator = createClipHeightAnimator(bottom.toFloat(), initialHeight)
            viewTopAnimator = createViewTopAnimator(originalTop, initialTop)
            contentAlphaAnimator = createContentAlphaAnimator(content, reversed = true)
            isReversed = false
        }
        clipHeightAnimator?.doOnEnd {
            animate {
                duration = 100L
                alpha(0f)
                withEndAction(onFinished)
            }
        }
        listOfNotNull(clipHeightAnimator, viewTopAnimator, contentAlphaAnimator).forEach {
            it.interpolator = outInterpolator
            if (isReversed) it.reverse() else it.start()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        clipHeight?.let { canvas?.clipRect(0f, 0f, width.toFloat(), it) }
        super.onDraw(canvas)
    }

    private fun createClipHeightAnimator(initialHeight: Float, finalHeight: Float = bottom.toFloat()) =
        ValueAnimator.ofFloat(initialHeight, finalHeight).apply {
            duration = clipHeightDuration
            interpolator = inInterpolator
            addUpdateListener {
                clipHeight = animatedValue()
                invalidate()
            }
        }

    private fun createViewTopAnimator(initialTop: Int, finalTop: Int = originalTop) =
        ValueAnimator.ofInt(initialTop, finalTop).apply {
            duration = topTravelDuration
            interpolator = inInterpolator
            addUpdateListener {
                top = animatedValue()
            }
        }

    private fun createContentAlphaAnimator(contentView: ViewGroup, reversed: Boolean = false) =
        ValueAnimator.ofFloat(reversed.toFloat(), (!reversed).toFloat()).apply {
            duration = contentAlphaDuration
            interpolator = inInterpolator
            addUpdateListener {
                contentView.children.forEach {
                    it.alpha = animatedValue()
                }
            }
        }

    private fun Boolean.toFloat() = if (this) 1f else 0f

}