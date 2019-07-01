package com.kondenko.funshop.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.jakewharton.rxbinding3.view.clicks
import com.kondenko.funshop.R
import kotlinx.android.synthetic.main.layout_payload.view.*

class PayloadView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val payloadLayout = View.inflate(context, R.layout.layout_payload, this)

    private var payloadMessage: String? = null

    /**
     * Determines whether the View hides itself when it's needed
     */
    var isSelfManaged = true

    var payload: Any? = null
        set(value) {
            if (isSelfManaged) isVisible = value != null
            payloadLayout.payloadTextViewPayload.text = payloadMessage?.format(value.toString())
            field = value
        }

    init {
        useAttributes(attrs, R.styleable.PayloadView, defStyleAttr, defStyleRes) {
            payloadMessage = getString(R.styleable.PayloadView_text)
        }
    }

    fun updateClicks() = payloadLayout.payloadButtonUpdate.clicks()
            .doOnNext { if (isSelfManaged) this.isGone = true }
            .map { KOptional(payload) }

    fun discardClicks() = payloadLayout.payloadButtonDiscard.clicks()
            .doOnNext { if (isSelfManaged) this.isGone = true }

}