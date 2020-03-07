package com.xephorium.crystalnote.ui.update

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.Note.Companion.NO_NOTE
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.CrystalNoteToast
import com.xephorium.crystalnote.ui.base.BaseActivity
import com.xephorium.crystalnote.ui.custom.ColorPickerDialog
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.custom.PasswordDialog
import com.xephorium.crystalnote.ui.custom.PasswordDialog.Companion.PasswordDialogListener
import com.xephorium.crystalnote.ui.home.HomeActivity
import com.xephorium.crystalnote.ui.utility.KeyboardUtility
import com.xephorium.crystalnote.ui.widget.NotesWidgetProvider
import kotlinx.android.synthetic.main.note_toolbar_layout.*

import kotlinx.android.synthetic.main.update_activity_layout.*
import androidx.core.app.ActivityCompat


class UpdateActivity() : BaseActivity(), UpdateContract.View {


    /*--- Variable Declarations ---*/

    lateinit var presenter: UpdatePresenter
    lateinit var optionsMenu: Menu

    private val noteId: Int
        get() = intent.getIntExtra(KEY_NOTE_ID, NO_NOTE)

    private val isLaunchFromWidget: Boolean
        get() = (intent.getBooleanExtra(KEY_LAUNCH_FROM_WIDGET, false))

    private val isLaunchFromSelect: Boolean
        get() = (intent.getBooleanExtra(KEY_LAUNCH_FROM_SELECT, false))


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_activity_layout)

        presenter = UpdatePresenter()
        presenter.noteRepository = NoteRoomRepository(this)
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)
        presenter.isInEditMode = noteId != NO_NOTE
        presenter.isLaunchFromWidget = isLaunchFromWidget
        presenter.isLaunchFromSelect = isLaunchFromSelect
        presenter.isFileWritePermissionGranted = checkFileWritePermission()
        presenter.noteId = noteId

        setupToolbar()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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

    override fun populateColor(color: Int) {
        toolbar.setColor(color)
    }

    override fun showTextUnderline() {
        textNoteContent.showUnderline()
    }

    override fun hideTextUnderline() {
        textNoteContent.hideUnderline()
    }

    override fun showMonospacedFont() {
        textNoteContent.useMonospacedFont()
    }

    override fun showLockMenuOption() {
        optionsMenu.findItem(R.id.option_lock).isVisible = true
        optionsMenu.findItem(R.id.option_unlock).isVisible = false
    }

    override fun showUnlockMenuOption() {
        optionsMenu.findItem(R.id.option_lock).isVisible = false
        optionsMenu.findItem(R.id.option_unlock).isVisible = true
    }

    override fun showColorPickerDialog() {
        val colorPickerDialog = ColorPickerDialog.Builder(this).create()
        colorPickerDialog.setTitle("Choose Note Color")
        colorPickerDialog.setColorPickerListener(object :
            ColorPickerDialog.Companion.ColorPickerListener {
            override fun onColorSelect(color: Int) {
                colorPickerDialog.dismiss()
                presenter.handleColorChange(color)
            }
        })
        colorPickerDialog.show()
    }

    override fun showSetNewPasswordDialog() {
        val setPasswordDialog = PasswordDialog.Builder(this).create()
        setPasswordDialog.setTitle("Choose Password")
        setPasswordDialog.setMessage("Once locked, your note will be accessible only by password.")
        setPasswordDialog.setButtonName("Set")
        setPasswordDialog.setPasswordDialogListener(object : PasswordDialogListener {
            override fun onPasswordProvided(password: String) {
                presenter.handleNewPasswordSet(password)
            }

            override fun verifyPassword(password: String): String {
                if (password.contains("\\s".toRegex())) {
                    return "Cannot contain whitespace"
                } else {
                    return ""
                }
            }
        })
        setPasswordDialog.show()
    }

    override fun showVerifyNewPasswordDialog(password: String) {
        val verifyPasswordDialog = PasswordDialog.Builder(this).create()
        verifyPasswordDialog.setTitle("Confirm Password")
        verifyPasswordDialog.setMessage("Please enter password again.")
        verifyPasswordDialog.setButtonName("Confirm")
        verifyPasswordDialog.setPasswordDialogListener(object : PasswordDialogListener {
            override fun onPasswordProvided(newPassword: String) {
                presenter.handleNewPasswordVerify(newPassword)
            }

            override fun verifyPassword(newPassword: String): String {
                if (password != newPassword) {
                    return "Password doesn't match"
                } else {
                    return ""
                }
            }
        })
        verifyPasswordDialog.show()
    }

    override fun showNoteLockedMessage() {
        CrystalNoteToast.showShort(this, "Note locked.")
    }

    override fun showRemovePasswordDialog(password: String) {
        val verifyPasswordDialog = PasswordDialog.Builder(this).create()
        verifyPasswordDialog.setTitle("Unlock Note")
        verifyPasswordDialog.setMessage("Enter password to unlock note.")
        verifyPasswordDialog.setButtonName("Unlock")
        verifyPasswordDialog.setPasswordDialogListener(object : PasswordDialogListener {
            override fun onPasswordProvided(password: String) {
                presenter.handleOldPasswordVerify()
            }

            override fun verifyPassword(newPassword: String): String {
                if (password != newPassword) {
                    return " "
                } else {
                    return ""
                }
            }
        })
        verifyPasswordDialog.show()
    }

    override fun showNoteUnlockedMessage() {
        CrystalNoteToast.showShort(this, "Note unlocked.")
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

    override fun showFileWritePermissionDeniedMessage() {
        CrystalNoteToast.showLong(this, "Export permission denied.")
    }

    override fun showExportDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(true)
        alertDialog.setTitle("Export Note")
        alertDialog.setMessage("Note will be saved as a .txt file in the Downloads folder.")
        alertDialog.setButton(BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(BUTTON_POSITIVE, "Confirm") { dialog, _ ->
            dialog.dismiss()
            presenter.handleExportConfirm()
        }
        alertDialog.show()
    }

    override fun showExportConfirmationMessage() {
        CrystalNoteToast.showLong(this, "Note exported.")
    }

    override fun navigateHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(KEY_FROM_UPDATE_ACTIVITY, true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun navigateBack() {
        finish()
    }

    override fun refreshWidget() {
        NotesWidgetProvider.refreshWidgets(this, application)
    }

    override fun hideKeyboard() {
        KeyboardUtility.hideKeyboard(this)
    }


    /*--- Permissions Methods ---*/

    override fun requestFileWritePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE),
            FILE_WRITE_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        results: IntArray
    ) {
        if (requestCode == FILE_WRITE_REQUEST_CODE && permissionRequestGranted(results)) {
            presenter.handleFileWritePermissionGranted()
        } else {
            presenter.handleFileWritePermissionDenied()
        }
    }

    private fun permissionRequestGranted(results: IntArray): Boolean {
        return results.isNotEmpty() && results[0] == PackageManager.PERMISSION_GRANTED
    }

    private fun checkFileWritePermission(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
    }


    /*--- Setup Methods ---*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_options, menu)

        // Determine Lock Items' Initial Visibility
        // Note: This should be handled in the presenter, but the onCreateOptionsMenu()
        //       method is called after attach, meaning we don't yet have a menu to
        //       manipulate. May the Code Gods forgive me.
        optionsMenu = menu
        if (presenter.password.isEmpty()) {
            optionsMenu.findItem(R.id.option_lock).isVisible = true
            optionsMenu.findItem(R.id.option_unlock).isVisible = false
        } else {
            optionsMenu.findItem(R.id.option_lock).isVisible = false
            optionsMenu.findItem(R.id.option_unlock).isVisible = true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_lock -> presenter.handleLockClick()
            R.id.option_unlock -> presenter.handleUnlockClick()
            R.id.option_export -> presenter.handleExportClick()
            R.id.option_delete -> presenter.handleDeleteClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.isEditMode = true
        toolbar.setLeftButtonImage(R.drawable.icon_back)
        toolbar.showColor()
        toolbar.setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
            override fun onButtonClick() = presenter.handleBackClick()
            override fun onColorClick() = presenter.handleColorClick()
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
        colorOrbToolbar.setOnClickListener { presenter.handleColorClick() }
    }


    /*--- Constants ---*/

    companion object {
        const val KEY_NOTE_ID = "NOTE_ID_KEY"
        const val KEY_FROM_UPDATE_ACTIVITY = "FROM_UPDATE_ACTIVITY_KEY"
        const val KEY_LAUNCH_FROM_WIDGET = "LAUNCH_FROM_WIDGET_KEY"
        const val KEY_LAUNCH_FROM_SELECT = "LAUNCH_FROM_SELECT_KEY"
        const val FILE_WRITE_REQUEST_CODE = 117
    }
}
