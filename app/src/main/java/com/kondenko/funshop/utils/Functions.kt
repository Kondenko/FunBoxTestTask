package com.kondenko.funshop.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.reactivestreams.Subscriber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Matcher

fun <T> LiveData<T>.subscribe(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) =
    observe(lifecycleOwner, Observer(action))

fun <T> Iterable<T>.replace(replacementItem: T, predicate: (T) -> Boolean) = map { if (predicate(it)) replacementItem else it  }

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