package com.xephorium.crystalnote.data.utility

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme


object CrystalNoteToast {

    fun showShort(context: Context, string: String) {
        val view: View
        val textView: TextView
        val toast: Toast = Toast.makeText(context, string, Toast.LENGTH_SHORT)

        view = toast.view
        view.setBackgroundResource(R.drawable.toast_background)
        textView = view.findViewById(android.R.id.message)
        textView.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextInvertedPrimary)
        textView.setShadowLayer(0f, 0f, 0f, 0)

        toast.show()
    }

    fun showLong(context: Context, string: String) {
        val view: View
        val textView: TextView
        val toast: Toast = Toast.makeText(context, string, Toast.LENGTH_LONG)

        view = toast.view
        view.setBackgroundResource(R.drawable.toast_background)
        textView = view.findViewById(android.R.id.message)
        textView.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextInvertedPrimary)
        textView.setShadowLayer(0f, 0f, 0f, 0)

        toast.show()
    }
}