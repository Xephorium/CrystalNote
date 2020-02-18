package com.xephorium.crystalnote.ui.update

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.data.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.ToolbarActivity
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.widget.NotesWidgetProvider

import kotlinx.android.synthetic.main.update_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_activity_layout.*

class UpdateActivity() : ToolbarActivity(), UpdateContract.View {


    /*--- Variable Declarations ---*/

    lateinit var presenter: UpdatePresenter

    private val initialName: String
        get() = intent.getStringExtra(KEY_NOTE_NAME) ?: ""

    private val isInEditMode: Boolean
        get() = (intent.getStringExtra(KEY_NOTE_NAME) ?: "").isNotBlank()

    private val isLaunchFromWidget: Boolean
        get() = (intent.getBooleanExtra(KEY_LAUNCH_FROM_WIDGET, false))


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.update_activity_layout)

        presenter = UpdatePresenter()
        presenter.noteRepository = NoteRepository(this)
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)
        presenter.isInEditMode = isInEditMode
        presenter.isLaunchFromWidget = isLaunchFromWidget
        presenter.initialName = initialName

        setupToolbar()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    public override fun onPause() {
        super.onPause()
        presenter.handleBackground()
    }

    public override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onBackPressed() {
        presenter.handleBackClick()
    }


    /*--- View Manipulation Methods ---*/

    override fun populateFields(name: String, content: String) {
        toolbar.setEditTextContent(name)
        textNoteContent.setText(content)
    }

    override fun showTextUnderline() {
        textNoteContent.showUnderline()
    }

    override fun hideTextUnderline() {
        textNoteContent.hideUnderline()
    }

    override fun showInvalidNameDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Invalid Name")
        alertDialog.setMessage("Note name is invalid and cannot be saved.")
        alertDialog.setButton(BUTTON_NEGATIVE, "Rename") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(BUTTON_POSITIVE, "Continue") { dialog, _ ->
            dialog.dismiss()
            presenter.handleDiscardChangesConfirm()
        }
        alertDialog.show()
    }

    override fun showDiscardChangesDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Discard Note")
        alertDialog.setMessage("Discard new note?")
        alertDialog.setButton(BUTTON_NEGATIVE, "No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(BUTTON_POSITIVE, "Yes") { dialog, _ ->
            dialog.dismiss()
            presenter.handleDiscardChangesConfirm()
        }
        alertDialog.show()
    }

    override fun showDeleteNoteDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Delete Note")
        alertDialog.setMessage("Are you sure?")
        alertDialog.setButton(BUTTON_NEGATIVE, "No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(BUTTON_POSITIVE, "Yes") { dialog, _ ->
            dialog.dismiss()
            presenter.handleDeleteConfirm()
        }
        alertDialog.show()
    }

    override fun navigateBack() {
        finish()
    }

    override fun refreshWidget() {
        NotesWidgetProvider.refreshWidgets(this, application)
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        toolbar.isEditMode = true
        toolbar.setLeftButtonImage(R.drawable.icon_back)
        toolbar.setRightButtonImage(R.drawable.icon_delete)
        toolbar.setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
            override fun onLeftButtonClick() = presenter.handleBackClick()
            override fun onRightButtonClick() = presenter.handleDeleteClick()
            override fun onTextChange(text: String) = presenter.handleNameTextChange(text)
        })
    }

    private fun setupClickListeners() {
        textNoteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(editable: Editable?) {
                presenter.handleContentTextChange(textNoteContent.text.toString())
            }
        })
    }


    /*--- ---*/

    companion object {
        const val KEY_NOTE_NAME = "NOTE_NAME_KEY"
        const val KEY_LAUNCH_FROM_WIDGET = "LAUNCH_FROM_WIDGET_KEY"
    }
}
