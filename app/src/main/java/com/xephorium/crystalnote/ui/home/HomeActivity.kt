package com.xephorium.crystalnote.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.DocumentsContract
import android.view.View
import android.view.ViewTreeObserver
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.CrystalNoteToast
import com.xephorium.crystalnote.databinding.HomeActivityLayoutBinding
import com.xephorium.crystalnote.ui.custom.CrystalNoteDialog
import com.xephorium.crystalnote.ui.colorpicker.ColorPickerDialogFragment
import com.xephorium.crystalnote.ui.colorpicker.ColorPickerDialogFragment.Companion.ColorPickerListener
import com.xephorium.crystalnote.ui.custom.NoteListView
import com.xephorium.crystalnote.ui.custom.NoteOptionsDialog
import com.xephorium.crystalnote.ui.custom.NoteOptionsDialog.Companion.NoteOptionsListener
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.custom.PasswordDialog
import com.xephorium.crystalnote.ui.custom.PasswordDialog.Companion.PasswordDialogListener
import com.xephorium.crystalnote.ui.drawer.DrawerActivity
import com.xephorium.crystalnote.ui.drawer.DrawerItem
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity.Companion.KEY_FROM_UPDATE_ACTIVITY
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity.Companion.KEY_NOTE_ID


class HomeActivity : DrawerActivity(), HomeContract.View {


    /*--- Variable Declarations ---*/

    private val fromUpdateActivity: Boolean
        get() = intent.getBooleanExtra(KEY_FROM_UPDATE_ACTIVITY, false)

    private lateinit var homeBinding: HomeActivityLayoutBinding

    lateinit var presenter: HomePresenter

    private var splashDelayComplete = false


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeActivityLayoutBinding.inflate(layoutInflater)
        setBoundViewAsContent(homeBinding)

        presenter = HomePresenter()
        presenter.noteRoomRepository = NoteRoomRepository(this)
        presenter.noteDiskRepository = NoteDiskRepository(this)
        presenter.fromUpdateActivity = fromUpdateActivity

        setupToolbar()
        setupDrawer()
        setupClickListeners()
        delaySplashScreenCompletion()
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

    override fun populateNoteList(notes: List<PreviewNote>) {
        homeBinding.listHomeNotes.visibility = View.VISIBLE
        homeBinding.textHomeEmpty.visibility = View.GONE
        homeBinding.listHomeNotes.populateNoteList(notes)
    }

    override fun showEmptyNotesList() {
        homeBinding.listHomeNotes.visibility = View.GONE
        homeBinding.textHomeEmpty.visibility = View.VISIBLE
    }

    override fun showNavigationDrawer() {
        openDrawer()
    }

    override fun showNoteOptionsDialog() {
        val dialog = ColorPickerDialogFragment(supportFragmentManager)
        dialog.setTitle("Select a Color")
        dialog.setButtonText("Select")
        dialog.setColorPickerListener(object : ColorPickerListener {
            override fun onColorSelect(color: Int) {
                CrystalNoteToast.showShort(this@HomeActivity, "Color: $color")
            }
        })
        dialog.showDialog()

//        val newColorPickerDialog = NewColorPickerDialog.Builder(this).create()
//        newColorPickerDialog.show()
//        newColorPickerDialog.setTitle("Pick a Color")

//        val noteOptionsDialog = NoteOptionsDialog.Builder(this).create()
//        noteOptionsDialog.hideUnlockOption()
//        noteOptionsDialog.setListener(object: NoteOptionsListener {
//            override fun onLockClick() = presenter.handleLockClick()
//            override fun onUnlockClick() = presenter.handleUnlockClick()
//            override fun onImportClick() = Unit
//            override fun onExportClick() = presenter.handleExportClick()
//            override fun onOpenClick() = Unit
//            override fun onRestoreClick() = Unit
//            override fun onDeleteClick() = presenter.handleDeleteClick()
//        })
//        noteOptionsDialog.show()
    }

    override fun showLockedNoteOptionsDialog() {
        val noteOptionsDialog = NoteOptionsDialog.Builder(this).create()
        noteOptionsDialog.hideLockOption()
        noteOptionsDialog.hideExportOption()
        noteOptionsDialog.setListener(object: NoteOptionsListener {
            override fun onLockClick() = presenter.handleLockClick()
            override fun onUnlockClick() = presenter.handleUnlockClick()
            override fun onImportClick() = Unit
            override fun onExportClick() = presenter.handleExportClick()
            override fun onOpenClick() = Unit
            override fun onRestoreClick() = Unit
            override fun onDeleteClick() = presenter.handleDeleteClick()
        })
        noteOptionsDialog.show()
    }

    override fun showSetNewPasswordDialog() {
        val setPasswordDialog = PasswordDialog.Builder(this).create()
        setPasswordDialog.show()
        setPasswordDialog.setTitle("Choose Password")
        setPasswordDialog.setMessage("Once locked, your note will be accessible only by password.")
        setPasswordDialog.setButtonName("Set")
        setPasswordDialog.setPasswordDialogListener(object : PasswordDialogListener {
            override fun onPasswordProvided(password: String) {
                presenter.handleNewPasswordSet(password)
            }

            override fun verifyPassword(password: String): String {
                return if (password.contains("\\s".toRegex())) "Cannot contain whitespace" else ""
            }
        })
    }

    override fun showVerifyNewPasswordDialog(password: String) {
        val verifyPasswordDialog = PasswordDialog.Builder(this).create()
        verifyPasswordDialog.show()
        verifyPasswordDialog.setTitle("Confirm Password")
        verifyPasswordDialog.setMessage("Please enter password again.")
        verifyPasswordDialog.setButtonName("Confirm")
        verifyPasswordDialog.setPasswordDialogListener(object : PasswordDialogListener {
            override fun onPasswordProvided(newPassword: String) {
                presenter.handleNewPasswordVerify(newPassword)
            }

            override fun verifyPassword(newPassword: String): String {
                return if (password != newPassword) "Password doesn't match" else ""
            }
        })
    }

    override fun showNoteLockedMessage() {
        CrystalNoteToast.showShort(this, "Note locked.")
    }

    override fun showRemovePasswordDialog(password: String) {
        val verifyPasswordDialog = PasswordDialog.Builder(this).create()
        verifyPasswordDialog.setShouldShowErrors(false)
        verifyPasswordDialog.show()
        verifyPasswordDialog.setTitle("Unlock Note")
        verifyPasswordDialog.setMessage("Enter password to unlock note.")
        verifyPasswordDialog.setButtonName("Unlock")
        verifyPasswordDialog.setPasswordDialogListener(object : PasswordDialogListener {
            override fun onPasswordProvided(password: String) {
                presenter.handleOldPasswordVerify()
            }

            override fun verifyPassword(newPassword: String): String {
                return if (password != newPassword) " " else ""
            }
        })
    }

    override fun showNoteUnlockedMessage() {
        CrystalNoteToast.showShort(this, "Note unlocked.")
    }

    override fun showFileWritePermissionDeniedMessage() {
        CrystalNoteToast.showLong(this, "Export permission denied.")
    }

    override fun showExportDialog(noteName: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, noteName)

            // If supported, set initial directory to downloads
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) putExtra(
                DocumentsContract.EXTRA_INITIAL_URI,
                NoteDiskRepository.getDownloadsDirectory()
            )
        }
        startActivityForResult(intent, UpdateNoteActivity.FILE_CREATE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == UpdateNoteActivity.FILE_CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                presenter.handleExportFileCreated(uri)
            }
        }
    }

    override fun showExportConfirmationMessage() {
        CrystalNoteToast.showLong(this, "Note exported.")
    }

    override fun showExportErrorMessage() {
        CrystalNoteToast.showLong(this, "Error exporting note.")
    }

    override fun showDeleteNoteDialog() {
        val deleteNoteDialog = CrystalNoteDialog.Builder(this).create()
        deleteNoteDialog.show()
        deleteNoteDialog.setTitle("Delete Note")
        deleteNoteDialog.setMessage("Note will be permanently deleted. Are you sure?")
        deleteNoteDialog.setPositiveButtonName("Confirm")
        deleteNoteDialog.setNegativeButtonName("Cancel")
        deleteNoteDialog.setListener(object : CrystalNoteDialog.Companion.CrystalNoteDialogListener {
            override fun onPositiveClick() {
                presenter.handleDeleteConfirm()
            }
            override fun onNegativeClick() = Unit
            override fun onBackClick() = Unit
        })
    }

    override fun showNoteDeletedMessage() {
        CrystalNoteToast.showShort(this, "Note deleted.")
    }

    override fun showNotePasswordDialog(password: String, id: Int) {
        val setPasswordDialog = PasswordDialog.Builder(this).create()
        setPasswordDialog.setShouldShowErrors(false)
        setPasswordDialog.show()
        setPasswordDialog.setTitle("Note Locked")
        setPasswordDialog.setMessage("Enter password to view note.")
        setPasswordDialog.setButtonName("Open")
        setPasswordDialog.setPasswordDialogListener(object : PasswordDialogListener {
            override fun onPasswordProvided(password: String) {
                presenter.handleNoteAuthenticate(id)
            }

            override fun verifyPassword(newPassword: String): String {
                return if (password != newPassword) " " else ""
            }
        })
    }

    override fun navigateToEditNote(id: Int) {
        val intent = Intent(this, UpdateNoteActivity::class.java)
        intent.putExtra(KEY_NOTE_ID, id)
        startActivity(intent)
    }

    override fun navigateToNewNote() {
        val intent = Intent(this, UpdateNoteActivity::class.java)
        startActivity(intent)
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        drawerBinding.toolbar.run {
            isEditMode = false
            setTitle(R.string.homeTitle)
            setLeftButtonImage(R.drawable.icon_menu)
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onLeftButtonClick() = presenter.handleMenuButtonClick()
                override fun onRightButtonClick() = Unit
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

    private fun setupDrawer() {
        val sharedPreferencesRepository = SharedPreferencesRepository(this)
        sharedPreferencesRepository.setSelectedDrawerButton(DrawerItem.Companion.DrawerButton.NOTES)
    }

    private fun setupClickListeners() {
        homeBinding.floatingActionButtonHome.setOnClickListener { presenter.handleNewNoteButtonClick() }
        homeBinding.listHomeNotes.noteListViewListener = object : NoteListView.NoteListViewListener {
            override fun onNoteClick(note: PreviewNote) = presenter.handleNoteClick(note)
            override fun onNoteLongClick(note: PreviewNote) = presenter.handleNoteLongClick(note)
            override fun onNoteListRefresh() = presenter.handleNoteListRefresh()
        }
    }

    private fun delaySplashScreenCompletion() {
        // No splash screen shown for API levels beneath 29 (Q)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Handler().postDelayed({ splashDelayComplete = true }, SPLASH_SCREEN_DELAY)
            val content: View = findViewById(android.R.id.content)
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        return if (splashDelayComplete) {
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        } else {
                            false
                        }
                    }
                }
            )
        }
    }


    /*--- Constants ---*/

    companion object {
        const val SPLASH_SCREEN_DELAY: Long = 250
        const val FILE_WRITE_REQUEST_CODE = 117
    }
}
