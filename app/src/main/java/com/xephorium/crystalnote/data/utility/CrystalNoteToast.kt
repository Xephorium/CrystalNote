package com.xephorium.crystalnote.data.utility

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.xephorium.crystalnote.R


object CrystalNoteToast {

    fun showShort(context: Context, string: String) {
        showToast(context, string, Toast.LENGTH_SHORT)
    }

    fun showLong(context: Context, string: String) {
        showToast(context, string, Toast.LENGTH_LONG)
    }

    private fun showToast(context: Context, string: String, length: Int) {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.toast_themed, null)
        layout.findViewById<TextView>(R.id.toastText).text = string

        val toast = Toast.makeText(context, string, length)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.duration = Toast.LENGTH_LONG
        toast.setView(layout)
        toast.show()
    }
}