package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding3.view.clicks
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
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

    private val disposables: CompositeDisposable by inject()

    private var good: Good? = null

    private val saveClicks = PublishSubject.create<Good>()
    private val cancelClicks = PublishSubject.create<Unit>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View =
        inflater.inflate(R.layout.fragment_backend_good, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        good = arguments?.getParcelable(attrGood)
        good.render()
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
            .map { parseGood() }
            .subscribe(saveClicks)
        backendButtonCancel.clicks()
            .subscribe(cancelClicks)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    fun setGood(good: Good?) {
        onItemUpdated(this.good, good)
    }

    fun saveClicks(): Observable<Good> = saveClicks

    fun cancelClicks(): Observable<Unit> = cancelClicks

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

    private fun parseGood() = Good(
        name = backendEditTextName.text.toString(),
        price = backendEditTextPrice.text.toString().toDouble(),
        quantity = backendEditTextQuantity.text.toString().toLong()
    )

}