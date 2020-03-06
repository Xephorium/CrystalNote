package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.content.DialogInterface.BUTTON_POSITIVE
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import kotlinx.android.synthetic.main.password_dialog_layout.*


class PasswordDialog private constructor(private val context: Context) {


    /*--- Variable Declarations ---*/

    private val alertDialog = AlertDialog.Builder(context, R.style.DialogTheme)
        .setView(R.layout.password_dialog_layout)
        .create()
    private var listener: PasswordDialogListener = DEFAULT_LISTENER


    /*--- Public Methods ---*/

    fun setTitle(title: String) {
        alertDialog.setTitle(title)
    }

    fun setMessage(message: String) {
        alertDialog.setMessage(message)
    }

    fun setButtonName(name: String) {
        alertDialog.setButton(BUTTON_POSITIVE, name) { dialog, _ ->
            listener.onPasswordProvided(alertDialog.textInputEditTextPassword.toString())
            dialog.dismiss()
        }
    }

    fun setPasswordDialogListener(listener: PasswordDialogListener) {
        this.listener = listener
    }

    fun show() {
        alertDialog.show()

        // Setup Password EditText Listener
        alertDialog.textInputEditTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(p0: Editable?) = validatePasswordField(p0.toString())
        })

        // Set Initial State
        validatePasswordField(alertDialog.textInputEditTextPassword.text.toString())
    }

    fun dismiss() {
        alertDialog.dismiss()
    }


    /*--- Private Methods ---*/

    private fun validatePasswordField(password: String) {
        val isEmptyPassword = (alertDialog.textInputEditTextPassword.text ?: "").isEmpty()
        val passwordError = listener.verifyPassword(password)
        val isInvalidPassword = passwordError.isNotEmpty()
        val button = alertDialog.getButton(BUTTON_POSITIVE)

        if (isEmptyPassword) {

            // Disable Button
            button.isEnabled = false
            button.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextTertiary)

            // Hide Error
            alertDialog.textInputLayoutPassword.error = ""


        } else if (isInvalidPassword) {

            // Disable Button
            button.isEnabled = false
            button.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextTertiary)

            // Show Error
            alertDialog.textInputLayoutPassword.error = passwordError


        } else {

            // Enable Button
            button.isEnabled = true
            button.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextPrimary)

            // Hide Error
            alertDialog.textInputLayoutPassword.error = ""

        }
    }


    /*--- Builder Class ---*/

    class Builder(val context: Context) {
        fun create() = PasswordDialog(context)
    }


    /*--- Constants ---*/

    companion object {
        private val DEFAULT_LISTENER = object : PasswordDialogListener {
            override fun onPasswordProvided(password: String) = Unit
            override fun verifyPassword(password: String): String = ""
        }

        interface PasswordDialogListener {
            fun onPasswordProvided(password: String)
            fun verifyPassword(password: String): String
        }
    }
}
