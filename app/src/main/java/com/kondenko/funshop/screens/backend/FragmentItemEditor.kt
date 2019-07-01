package com.kondenko.funshop.screens.backend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding3.view.clicks
import com.kondenko.funshop.R
import com.kondenko.funshop.entities.Good
import kotlinx.android.synthetic.main.fragment_backend_good.*
import timber.log.Timber

class FragmentItemEditor : Fragment() {

    private val attrGood = "good"

    var good: Good? = null
        set(value) {
            field = value
            arguments?.putParcelable(attrGood, value)
            value.render()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View =
            inflater.inflate(R.layout.fragment_backend_good, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        good = savedInstanceState?.getParcelable(attrGood)
        Timber.d("Received $good")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(attrGood, good)
        super.onSaveInstanceState(outState)
    }

    fun saveClicks() = backendButtonSave.clicks().map { parseGood() }

    fun cancelClicks() = backendButtonCancel.clicks()

    private fun Good?.render() {
        backendEditTextName.setText(this?.name)
        backendEditTextQuantity.setText(this?.quantity?.toString())
        backendEditTextPrice.setText(this?.price?.toString())
    }

    private fun parseGood() = Good(
            name = backendEditTextName.text.toString(),
            quantity = backendEditTextQuantity.text.toString().toLong(),
            price = backendEditTextPrice.text.toString().toDouble()
    )

}