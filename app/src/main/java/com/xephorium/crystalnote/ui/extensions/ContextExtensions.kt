package com.xephorium.crystalnote.ui.extensions

import android.content.Context
import android.util.TypedValue



fun Context.getThemeColor(resId: Int) : Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}