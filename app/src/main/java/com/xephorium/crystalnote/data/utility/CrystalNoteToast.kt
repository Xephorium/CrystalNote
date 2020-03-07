package com.xephorium.crystalnote.data.utility

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import android.widget.Toast
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme


object CrystalNoteToast {

    fun showShort(context: Context, string: String) {
        showToast(context, string, Toast.LENGTH_SHORT)
    }

    fun showLong(context: Context, string: String) {
        showToast(context, string, Toast.LENGTH_LONG)
    }

    private fun showToast(context: Context, string: String, length: Int) {

        // Create Necessary Variables
        val toast: Toast = Toast.makeText(context, string, length)
        val view = toast.view
        val textView = view.findViewById(android.R.id.message) as TextView
        val padding = context.resources.getDimensionPixelSize(R.dimen.paddingBig)
        val oldLayoutParams = textView.layoutParams as ViewGroup.MarginLayoutParams
        val newLayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        newLayoutParams.setMargins( 0, oldLayoutParams.topMargin, 0, oldLayoutParams.bottomMargin)

        // Configure Toast
        view.setBackgroundResource(R.drawable.toast_background)
        textView.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextInvertedPrimary)
        textView.layoutParams = newLayoutParams
        textView.setPadding(padding, 0, padding, 0)

        // Show Toast
        toast.show()
    }
}