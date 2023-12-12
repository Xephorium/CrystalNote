package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.content.DialogInterface.BUTTON_POSITIVE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme


class PasswordDialog private constructor(private val context: Context) {


    /*--- Variable Declarations ---*/

    private val alertDialog = AlertDialog.Builder(context, R.style.DialogTheme)
        .setView(R.layout.password_dialog_layout)
        .create()
    private var listener: PasswordDialogListener = DEFAULT_LISTENER
    private var shouldShowErrors: Boolean = true


    /*--- Public Methods ---*/

    fun setTitle(title: String) {
        alertDialog.findViewById<TextView>(R.id.textPasswordTitle)?.text = title
    }

    fun setMessage(message: String) {
        if (message.isNotEmpty()) {
            alertDialog.findViewById<TextView>(R.id.textPasswordMessage)?.run {
                visibility = View.VISIBLE
                text = message
            }
        } else {
            alertDialog.findViewById<TextView>(R.id.textPasswordMessage)?.run {
                visibility = View.GONE
            }
        }
    }

    fun setButtonName(name: String) {
        alertDialog.findViewById<AppCompatButton>(R.id.buttonPasswordPositive)?.text = name
        alertDialog.findViewById<AppCompatButton>(R.id.buttonPasswordPositive)?.setOnClickListener {
            alertDialog.findViewById<TextInputEditText>(R.id.textInputEditTextPassword)?.let {
                listener.onPasswordProvided(it.text.toString())
            }
            alertDialog.dismiss()
        }
    }

    fun setPasswordDialogListener(listener: PasswordDialogListener) {
        this.listener = listener
    }

    fun setShouldShowErrors(show: Boolean) {
        shouldShowErrors = show
    }

    fun show() {
        alertDialog.show()

        setupDialogAppearance()
        setupEventListeners()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }


    /*--- Private Methods ---*/

    private fun setupDialogAppearance() {
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupEventListeners() {

        // Dismiss on Background Click
        alertDialog.findViewById<ConstraintLayout>(R.id.dialogBackground)?.setOnClickListener {
            alertDialog.dismiss()
        }

        // Setup Password EditText Listener
        alertDialog.findViewById<TextInputEditText>(R.id.textInputEditTextPassword)?.run {
            addTextChangedListener (object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
                override fun afterTextChanged(p0: Editable?) = validatePasswordField(p0.toString())
            })
            validatePasswordField(text.toString())
        }
    }

    private fun validatePasswordField(password: String) {
        alertDialog.findViewById<TextInputEditText>(R.id.textInputEditTextPassword)?.run {
            val isEmptyPassword = (text ?: "").isEmpty()
            val passwordError = listener.verifyPassword(password)
            val isInvalidPassword = passwordError.isNotEmpty()
            val button = alertDialog.findViewById<AppCompatButton>(R.id.buttonPasswordPositive)

            // No Password Entered
            if (isEmptyPassword) {
                button?.isEnabled = false
                button?.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextSecondary)
                error = null // Hide Error

            // Password Invalid
            } else if (isInvalidPassword) {
                button?.isEnabled = false
                button?.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextSecondary)
                if (shouldShowErrors) error = passwordError // Show Error

            // Password Valid
            } else {
                button?.isEnabled = true
                button?.setTextColor(CrystalNoteTheme.fromCurrentTheme(context).colorTextPrimary)
                error = null // Hide Error

            }
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
