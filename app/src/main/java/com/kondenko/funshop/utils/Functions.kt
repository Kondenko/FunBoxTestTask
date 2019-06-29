package com.kondenko.funshop.utils

import android.view.View
import android.view.ViewPropertyAnimator
import org.reactivestreams.Subscriber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Matcher

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

fun <R> InputStream.linesToPublisher(publisher: Subscriber<R>, transform: (String) -> R) {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(InputStreamReader(this))
        do {
            val line = reader.readLine()
            val good = transform(line)
            publisher.onNext(good)
        } while (line != null)
    } catch (e: IOException) {
        publisher.onError(e)
    } finally {
        try {
            reader?.close()
        } catch (e: IOException) {
            publisher.onError(e)
        }
    }
}