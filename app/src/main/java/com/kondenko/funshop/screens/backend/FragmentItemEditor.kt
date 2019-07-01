package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding3.view.clicks
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import com.kondenko.funshop.utils.findFirst
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_backend_good.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class FragmentItemEditor : Fragment() {

    private val attrGood = "good"

    private val disposables: CompositeDisposable by inject()

    private var good: Good? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View =
            inflater.inflate(R.layout.fragment_backend_good, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        good = savedInstanceState?.getParcelable(attrGood)
        payloadViewQuantity.apply {
            disposables += discardClicks().subscribeBy(onError = Timber::e)
            disposables += updateClicks().subscribeBy(onError = Timber::e) {
                (it.value as? Long)?.let {
                    onItemUpdated(null, good?.copy(quantity = it))
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(attrGood, good)
        super.onSaveInstanceState(outState)
    }

    fun setGood(good: Good?) {
        onItemUpdated(this.good, good)
    }

    fun saveClicks() = backendButtonSave.clicks().map { parseGood() }

    fun cancelClicks() = backendButtonCancel.clicks()

    private fun onItemUpdated(old: Good?, new: Good?) {
        val newIsPresent = new != null
        if (old != null && newIsPresent) {
            val payloads = old.getPayloads(new)
            payloadViewQuantity.apply {
                val quantityPayload = payloads!!.findFirst<Good.Payload.Quantity>()
                payload = quantityPayload?.new
            }
        } else if (newIsPresent) {
            new.render()
            good = new
            arguments?.putParcelable(attrGood, new)
        }
    }

    private fun Good?.render() {
        backendEditTextName.setText(this?.name)
        backendEditTextPrice.setText(this?.price?.toString())
        backendEditTextQuantity.setText(this?.quantity?.toString())
    }

    private fun parseGood() = Good(
            name = backendEditTextName.text.toString(),
            price = backendEditTextPrice.text.toString().toDouble(),
            quantity = backendEditTextQuantity.text.toString().toLong()
    )

}