package com.xephorium.crystalnote.ui.utility

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat

object DisplayUtils {


    /*--- Public Utility Methods ---*/

    fun getAppHeight(activity: Activity): Int {
        val rect = Rect()
        val rootView = activity.window.decorView
        rootView.getWindowVisibleDisplayFrame(rect)
        return rect.bottom - rect.top
    }

    fun getAppWidth(activity: Activity): Int {
        val rect = Rect()
        val rootView = activity.window.decorView
        rootView.getWindowVisibleDisplayFrame(rect)
        return rect.right - rect.left
    }

    fun getDisplayHeight(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }

    fun getDisplayWidth(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    fun getStatusBarHeight(activity: Activity): Int {
        return activity.resources.getDimensionPixelSize(
                activity.resources.getIdentifier("status_bar_height", "dimen", "android"))
    }

    fun getAbsoluteViewTop(view: View): Int {
        val coordinates = IntArray(2)
        view.getLocationOnScreen(coordinates)
        return coordinates[1]
    }
}