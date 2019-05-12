package com.xephorium.crystalnote.ui.utility

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {


    /*--- Public Utility Methods ---*/

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }
    }

    fun getKeyboardHeight(activity: Activity): Int {
        val appHeight = DisplayUtils.getAppHeight(activity)
        val displayHeight = DisplayUtils.getDisplayHeight(activity)
        val statusBarHeight = DisplayUtils.getStatusBarHeight(activity)
        return displayHeight - (appHeight + statusBarHeight)
    }
}
