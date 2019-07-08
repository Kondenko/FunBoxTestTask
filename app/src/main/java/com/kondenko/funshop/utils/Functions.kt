package com.kondenko.funshop.utils

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.Toast
import androidx.annotation.StyleableRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kondenko.funshop.R
import org.reactivestreams.Subscriber
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Matcher

fun <T> LiveData<T>.subscribe(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) =
        observe(lifecycleOwner, Observer(action))

fun <T> Iterable<T>.replace(replacementItem: T, predicate: (T) -> Boolean) = map { if (predicate(it)) replacementItem else it }

inline fun <reified T> Iterable<Any>.find(): T? = find { it is T } as? T

fun ViewPropertyAnimator.scale(value: Float): ViewPropertyAnimator = scaleX(value).scaleY(value)

inline fun <reified T> ValueAnimator.animatedValue() = animatedValue as T

inline fun View.animate(animation: ViewPropertyAnimator.() -> Unit) {
    animate().apply(animation).start()
}

fun Matcher.allSubgroups(): Map<String?, String?> {
    val groups = mutableMapOf<String?, String?>()
    while (find()) {
        for (j in 1..groupCount()) {
            group(j)?.let {
                groups += group() to it
            }
        }
    }
    return groups
}

fun InputStream.parseLines(publisher: Subscriber<in String>) {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(InputStreamReader(this))
        do {
            val line = reader.readLine()
            line?.let { publisher.onNext(it) }
        } while (line != null)
    } catch (e: IOException) {
        publisher.onError(e)
    } finally {
        publisher.onComplete()
        try {
            reader?.close()
        } catch (e: IOException) {
            publisher.onError(e)
        }
    }
}

inline fun FragmentManager.transaction(actions: FragmentTransaction.() -> Unit) =
        beginTransaction().apply(actions).commit()

fun View.useAttributes(attrs: AttributeSet?, @StyleableRes styleable: IntArray, defStyleAttr: Int = 0, defStyleRes: Int = 0, actions: TypedArray.() -> Unit) {
    attrs?.let {
        with(context.obtainStyledAttributes(it, styleable, defStyleAttr, defStyleRes)) {
            actions()
            recycle()
        }
    }
}

fun Context.showError(throwable: Throwable, messageRes: Int = R.string.all_error_generic) {
    Timber.e(throwable)
    Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
}