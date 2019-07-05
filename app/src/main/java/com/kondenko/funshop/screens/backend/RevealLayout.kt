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

    fun reveal(initialHeight: Float, initialTop: Float, content: ViewGroup) {
        post {
            clipHeightAnimator = createClipHeightAnimator(initialHeight).also(ValueAnimator::start)
            viewTopAnimator = createViewTopAnimator(initialTop).also(ValueAnimator::start)
            contentAlphaAnimator = createContentAlphaAnimator(content).also(ValueAnimator::start)
        }
    }

    fun hide(onFinished: () -> Unit) {
        clipHeightAnimator?.doOnEnd {
            animate {
                duration = 100L
                alpha(0f)
                withEndAction(onFinished)
            }
        }
        listOfNotNull(clipHeightAnimator, viewTopAnimator, contentAlphaAnimator).forEach {
            it.interpolator = outInterpolator
            it.reverse()
        }
        clipHeight = null
        alpha = 1f
        top = originalTop
    }

    override fun onDraw(canvas: Canvas?) {
        clipHeight?.let { canvas?.clipRect(0f, 0f, width.toFloat(), it) }
        super.onDraw(canvas)
    }

    private fun createClipHeightAnimator(initialHeight: Float) =
        ValueAnimator.ofFloat(initialHeight, bottom.toFloat()).apply {
            duration = clipHeightDuration
            interpolator = inInterpolator
            addUpdateListener {
                clipHeight = animatedValue()
                invalidate()
            }
        }

    private fun createViewTopAnimator(initialTop: Float) =
        ValueAnimator.ofInt(initialTop.toInt(), originalTop).apply {
            duration = topTravelDuration
            interpolator = inInterpolator
            addUpdateListener {
                top = animatedValue()
            }
        }

    private fun createContentAlphaAnimator(contentView: ViewGroup) =
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = contentAlphaDuration
            interpolator = inInterpolator
            addUpdateListener {
                contentView.children.forEach {
                    it.alpha = animatedValue()
                }
            }
        }

}