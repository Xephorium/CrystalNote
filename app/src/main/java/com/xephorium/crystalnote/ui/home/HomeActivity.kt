package com.xephorium.crystalnote.ui.home

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.utility.CrystalNoteToast
import com.xephorium.crystalnote.ui.drawer.DrawerActivity
import com.xephorium.crystalnote.ui.custom.NoteListView
import com.xephorium.crystalnote.ui.custom.NoteOptionsDialog
import com.xephorium.crystalnote.ui.custom.NoteOptionsDialog.Companion.NoteOptionsListener
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.custom.PasswordDialog
import com.xephorium.crystalnote.ui.custom.PasswordDialog.Companion.PasswordDialogListener
import com.xephorium.crystalnote.ui.update.UpdateActivity
import com.xephorium.crystalnote.ui.update.UpdateActivity.Companion.KEY_FROM_UPDATE_ACTIVITY
import com.xephorium.crystalnote.ui.update.UpdateActivity.Companion.KEY_NOTE_ID

import kotlinx.android.synthetic.main.home_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_activity_layout.*

class HomeActivity : DrawerActivity(), HomeContract.View {


    /*--- Variable Declarations ---*/

    private val fromUpdateActivity: Boolean
        get() = intent.getBooleanExtra(KEY_FROM_UPDATE_ACTIVITY, false)

    lateinit var presenter: HomePresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.home_activity_layout)

        presenter = HomePresenter()
        presenter.noteRoomRepository = NoteRoomRepository(this)
        presenter.noteDiskRepository = NoteDiskRepository()
        presenter.fromUpdateActivity = fromUpdateActivity
        presenter.isFileWritePermissionGranted = checkFileWritePermission()

        setupToolbar()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }


    /*--- View Manipulation Methods ---*/

    override fun populateNoteList(notes: List<Note>) {
        listHomeNotes.visibility = View.VISIBLE
        textHomeEmpty.visibility = View.GONE
        listHomeNotes.populateNoteList(notes)
    }

    override fun showEmptyNotesList() {
        listHomeNotes.visibility = View.GONE
        textHomeEmpty.visibility = View.VISIBLE
    }

    override fun showNavigationDrawer() {
        openDrawer()
    }

    override fun showNoteOptionsDialog() {
        val noteOptionsDialog = NoteOptionsDialog.Builder(this).create()
        noteOptionsDialog.hideUnlockOption()
        noteOptionsDialog.setListener(object: NoteOptionsListener {
            override fun onLockClick() = presenter.handleLockClick()
            override fun onUnlockClick() = presenter.handleUnlockClick()
            override fun onExportClick() = presenter.handleExportClick()
            override fun onDeleteClick() = presenter.handleDeleteClick()
        })
        noteOptionsDialog.show()
    }

    override fun showLockedNoteOptionsDialog() {
        val noteOptionsDialog = NoteOptionsDialog.Builder(this).create()
        noteOptionsDialog.hideLockOption()
        noteOptionsDialog.hideExportOption()
        noteOptionsDialog.setListener(object: NoteOptionsListener {
            override fun onLockClick() = presenter.handleLockClick()
            override fun onUnlockClick() = presenter.handleUnlockClick()
            override fun onExportClick() = presenter.handleExportClick()
            override fun onDeleteClick() = presenter.handleDeleteClick()
        })
        noteOptionsDialog.show()
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

    override fun showFileWritePermissionDeniedMessage() {
        CrystalNoteToast.showLong(this, "Export permission denied.")
    }

    override fun showExportDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(true)
        alertDialog.setTitle("Export Note")
        alertDialog.setMessage("Note will be saved as a .txt file in the Downloads folder.")
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm") { dialog, _ ->
            dialog.dismiss()
            presenter.handleExportConfirm()
        }
        alertDialog.show()
    }

    override fun showExportConfirmationMessage() {
        CrystalNoteToast.showLong(this, "Note exported.")
    }

    override fun showDeleteNoteDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Delete Note")
        alertDialog.setMessage("Are you sure?")
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes") { dialog, _ ->
            dialog.dismiss()
            presenter.handleDeleteConfirm()
        }
        alertDialog.show()
    }

    override fun showNotePasswordDialog(password: String, id: Int) {
        val setPasswordDialog = PasswordDialog.Builder(this).create()
        setPasswordDialog.setTitle("Note Locked")
        setPasswordDialog.setMessage("Enter password to view note.")
        setPasswordDialog.setButtonName("Open")
        setPasswordDialog.setPasswordDialogListener(object : PasswordDialogListener {
            override fun onPasswordProvided(password: String) {
                presenter.handleNoteAuthenticate(id)
            }

            override fun verifyPassword(newPassword: String): String {
                if (password != newPassword) {
                    return " "
                } else {
                    return ""
                }
            }
        })
        setPasswordDialog.show()
    }

    override fun navigateToEditNote(id: Int) {
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra(KEY_NOTE_ID, id)
        startActivity(intent)
    }

    override fun navigateToNewNote() {
        val intent = Intent(this, UpdateActivity::class.java)
        startActivity(intent)
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


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        toolbar.isEditMode = false
        toolbar.setTitle(R.string.homeTitle)
        toolbar.setLeftButtonImage(R.drawable.icon_menu)
        toolbar.setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
            override fun onButtonClick() = presenter.handleMenuButtonClick()
            override fun onColorClick() = Unit
            override fun onTextChange(text: String) = Unit
        })
    }

    private fun setupClickListeners() {
        floatingActionButtonHome.setOnClickListener { presenter.handleNewNoteButtonClick() }
        listHomeNotes.noteListViewListener = object : NoteListView.NoteListViewListener {
            override fun onNoteClick(note: Note) = presenter.handleNoteClick(note)
            override fun onNoteLongClick(note: Note) = presenter.handleNoteLongClick(note)
            override fun onNoteListRefresh() = presenter.handleNoteListRefresh()
        }
    }


    /*--- Constants ---*/

    companion object {
        const val FILE_WRITE_REQUEST_CODE = 117
    }
}
