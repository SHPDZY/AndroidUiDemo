package com.example.libcommon.utils

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.Utils


@ColorInt
fun Int.getColor(context: Context? = null): Int {
    return if (context == null) {
        ContextCompat.getColor(Utils.getApp(), this)
    } else {
        ContextCompat.getColor(context, this)
    }
}

fun Int.get(context: Context? = null): String {
    return if (context == null) {
        Utils.getApp().getString(this)
    } else {
        context.getString(this)
    }
}

fun Int.formatD(formatD: Int, context: Context? = null): String {
    return if (context == null) {
        String.format(Utils.getApp().getString(this), formatD)
    } else {
        String.format(context.getString(this), formatD)
    }
}

fun Int.format(vararg formatS: String?, context: Context? = null): String {
    return if (context == null) {
        String.format(Utils.getApp().getString(this), *formatS)
    } else {
        String.format(context.getString(this),*formatS)
    }
}

fun Int.fori(invoke:(index:Int)->Unit){
    for (i in 0..this) {
        invoke.invoke(i)
    }
}

fun Int.forr(invoke:(index:Int)->Unit){
    for (i in this downTo 1) {
        invoke.invoke(i)
    }
}