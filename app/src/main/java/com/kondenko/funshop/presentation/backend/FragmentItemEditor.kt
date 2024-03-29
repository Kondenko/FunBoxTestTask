package com.kondenko.funshop.presentation.backend

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnCancel
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.KOptional
import com.kondenko.funshop.utils.animatedValue
import com.kondenko.funshop.utils.find
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_backend_good.*
import kotlinx.android.synthetic.main.fragment_backend_good.view.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class FragmentItemEditor : Fragment() {

    private val attrGood = "good"

    private var good: Good? = null

    private val disposables: CompositeDisposable by inject()
    private val saveClicks = PublishSubject.create<Good>()
    private val cancelClicks = PublishSubject.create<Unit>()

    private var pulsationAnimator: ValueAnimator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View =
        inflater.inflate(R.layout.fragment_backend_good, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        good = arguments?.getParcelable(attrGood)
        good.render()
        setupUi()
    }

    fun setGood(good: Good?) {
        onItemUpdated(this.good, good)
        if (view != null) setupUi()
    }

    fun saveClicks(): Observable<Good> = saveClicks

    fun cancelClicks(): Observable<Unit> = cancelClicks

    fun setLoading(isLoading: Boolean) {
        view?.backendButtonSave?.apply {
            isEnabled = !isLoading
            if (isLoading) pulsate()
            else pulsationAnimator?.cancel()
        }
    }

    private fun View.pulsate() {
        val originalAlpha = alpha
        val alphaLow = (alpha / 2).coerceAtLeast(0f)
        pulsationAnimator = ValueAnimator.ofFloat(alpha, alphaLow).apply {
            addUpdateListener { alpha = animatedValue() }
            doOnCancel { alpha = originalAlpha }
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = DecelerateInterpolator()
            duration = 600
            start()
        }
    }

    fun reveal(y: Int, height: Float) {
        view?.backendGoodEditorRevealView?.reveal(height, y, backendGoodEditorLayoutContent)
    }

    fun hide(y: Int, height: Float, onFinished: () -> Unit) {
        view?.backendGoodEditorRevealView?.hide(height, y, backendGoodEditorLayoutContent, onFinished)
    }

    private fun setupUi() {
        disposables.clear()

        payloadViewName.apply {
            disposables += discardClicks().subscribeBy(onError = Timber::e)
            disposables += updateClicks<String>().subscribeBy(onError = Timber::e) {
                it.value?.let { onItemUpdated(new = good?.copy(name = it)) }
            }
        }
        payloadViewPrice.apply {
            disposables += discardClicks().subscribeBy(onError = Timber::e)
            disposables += updateClicks<Double>().subscribeBy(onError = Timber::e) {
                it.value?.let { onItemUpdated(new = good?.copy(price = it)) }
            }
        }
        payloadViewQuantity.apply {
            disposables += discardClicks().subscribeBy(onError = Timber::e)
            disposables += updateClicks<Long>().subscribeBy(onError = Timber::e) {
                it.value?.let { onItemUpdated(new = good?.copy(quantity = it)) }
            }
        }

        backendButtonSave.clicks()
            .map { KOptional(parseGood()) }
            .filter { it.isNotEmpty() }
            .map { it.value }
            .subscribe(saveClicks)

        backendButtonCancel.clicks()
            .subscribe(cancelClicks)

        val nameLengthValidator = textInputLayoutName.validate()
        val priceLengthValidator = textInputLayoutPrice.validate()
        val quantityLengthValidator = textInputLayoutQuantity.validate()
        val validators = arrayOf(nameLengthValidator, priceLengthValidator, quantityLengthValidator)

        disposables += Observable.combineLatest(validators) { it.all { it == true } }
            .startWith(good != null)
            .subscribe(backendButtonSave::setEnabled, Timber::e)
    }

    private fun TextInputLayout.validate(condition: (CharSequence) -> Boolean = { it.isNotBlank() }) =
        this.editText?.let { et ->
            et.textChanges()
                .skip(if (good == null) 1 else 0)
                .map { condition(it) }
                .doOnDispose { error = null }
                .doOnNext { isValid ->
                    error = if (!isValid) getString(R.string.backend_error_field_empty) else null
                    isErrorEnabled = !isValid
                }
        } ?: Observable.error(NullPointerException("TextInputLayout is null"))

    private fun onItemUpdated(old: Good? = null, new: Good?) {
        if (old != null && old.id == new?.id) {
            val payloads = old.getPayloads(new)
            payloadViewName?.payload = payloads.find<Good.Payload.Name>()?.new
            payloadViewPrice?.payload = payloads.find<Good.Payload.Price>()?.new
            payloadViewQuantity?.payload = payloads.find<Good.Payload.Quantity>()?.new
        } else {
            good = new
            new.render()
            arguments = bundleOf(attrGood to new)
        }
    }

    private fun Good?.render() {
        view?.let {
            it.backendEditTextName.setText(this?.name)
            it.backendEditTextPrice.setText(this?.price?.toString())
            it.backendEditTextQuantity.setText(this?.quantity?.toString())
        }
    }

    private fun parseGood(): Good? {
        val name = backendEditTextName.text.toString()
        val price = backendEditTextPrice.text.toString().toDoubleOrNull()
        val quantity = backendEditTextQuantity.text.toString().toLongOrNull()
        return if (price != null && quantity != null) {
            Good(
                id = good?.id,
                name = name,
                price = price,
                quantity = quantity
            )
        } else {
            null
        }
    }

}