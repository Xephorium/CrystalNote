package com.xephorium.crystalnote.ui.utility

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtility {


    /*--- Public Utility Methods ---*/

    fun hideKeyboard(activity: Activity) {
        activity.currentFocus?.let {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun showKeyboard(activity: Activity) {
        activity.currentFocus?.let {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(it, 0)
        }
    }

    fun getKeyboardHeight(activity: Activity): Int {
        val appHeight = DisplayUtility.getAppHeight(activity)
        val displayHeight = DisplayUtility.getDisplayHeight(activity)
        val statusBarHeight = DisplayUtility.getStatusBarHeight(activity)
        return displayHeight - (appHeight + statusBarHeight)
    }
}
