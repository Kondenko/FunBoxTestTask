package com.kondenko.funshop.utils

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.annotation.StyleableRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.reactivestreams.Subscriber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import java.util.regex.Matcher

fun <T> LiveData<T>.subscribe(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) =
        observe(lifecycleOwner, Observer(action))

fun <T> Iterable<T>.replace(replacementItem: T, predicate: (T) -> Boolean) = map { if (predicate(it)) replacementItem else it }

fun View.animate(animation: ViewPropertyAnimator.() -> ViewPropertyAnimator) {
    animate().animation().start()
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

fun FragmentManager.transaction(actions: FragmentTransaction.() -> FragmentTransaction) =
        beginTransaction().actions().commit()

inline fun <reified T> Iterable<Any>.find(): T? = find { it is T } as? T

fun View.useAttributes(attrs: AttributeSet?, @StyleableRes styleable: IntArray, defStyleAttr: Int = 0, defStyleRes: Int = 0, actions: TypedArray.() -> Unit) {
    attrs?.let {
        with(context.obtainStyledAttributes(it, styleable, defStyleAttr, defStyleRes)) {
            actions()
            recycle()
        }
    }
}

fun <T> Stack<T>.peekOrNull(): T? = if (isNotEmpty()) peek() else null