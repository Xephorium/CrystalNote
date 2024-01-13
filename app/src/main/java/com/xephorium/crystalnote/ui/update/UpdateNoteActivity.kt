package com.xephorium.crystalnote.ui.update

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.Note.Companion.NO_NOTE
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.CrystalNoteToast
import com.xephorium.crystalnote.databinding.UpdateActivityLayoutBinding
import com.xephorium.crystalnote.ui.base.BaseActivity
import com.xephorium.crystalnote.ui.colorpicker.ColorPickerDialogFragment
import com.xephorium.crystalnote.ui.colorpicker.ColorPickerDialogFragment.Companion.ColorPickerListener
import com.xephorium.crystalnote.ui.custom.CrystalNoteDialog
import com.xephorium.crystalnote.ui.custom.NoteOptionsDialog
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.custom.PasswordDialog
import com.xephorium.crystalnote.ui.custom.PasswordDialog.Companion.PasswordDialogListener
import com.xephorium.crystalnote.ui.home.HomeActivity
import com.xephorium.crystalnote.ui.utility.KeyboardUtility
import com.xephorium.crystalnote.ui.widget.NotesWidgetProvider


class UpdateNoteActivity() : BaseActivity(), UpdateNoteContract.View {


    /*--- Variable Declarations ---*/

    private lateinit var binding: UpdateActivityLayoutBinding

    lateinit var presenter: UpdateNotePresenter

    private val noteId: Int
        get() = intent.getIntExtra(KEY_NOTE_ID, NO_NOTE)

    private val isLaunchFromWidget: Boolean
        get() = (intent.getBooleanExtra(KEY_LAUNCH_FROM_WIDGET, false))

    private val isLaunchFromSelect: Boolean
        get() = (intent.getBooleanExtra(KEY_LAUNCH_FROM_SELECT, false))

    private val isLaunchFromUpdateFile: Boolean
        get() = (intent.getBooleanExtra(KEY_LAUNCH_FROM_UPDATE_FILE, false))


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UpdateActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = UpdateNotePresenter()
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)
        presenter.noteRoomRepository = NoteRoomRepository(this)
        presenter.noteDiskRepository = NoteDiskRepository(this)
        presenter.isInEditMode = noteId != NO_NOTE
        presenter.isLaunchFromWidget = isLaunchFromWidget
        presenter.isLaunchFromSelect = isLaunchFromSelect
        presenter.isLaunchFromUpdateFile = isLaunchFromUpdateFile
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


    /*--- View Manipulation Methods ---*/

    override fun populateFields(name: String, content: String) {
        binding.toolbar.setEditTextContent(name)
        binding.textNoteContent.setText(content)
    }

    override fun populateColor(color: Int) {
        binding.toolbar.setColor(color)
    }

    override fun showTextUnderline() {
        binding.textNoteContent.showUnderline()
    }

    override fun hideTextUnderline() {
        binding.textNoteContent.hideUnderline()
    }

    override fun showBottomButton() {
        binding.actionButtonBottom.visibility = View.VISIBLE
    }

    override fun hideBottomButton() {
        binding.actionButtonBottom.visibility = View.GONE
    }

    override fun showColorOrb() {
        binding.toolbar.showColor()
    }

    override fun hideColorOrb() {
        binding.toolbar.hideColor()
    }

    override fun showMonospacedFont() {
        binding.textNoteContent.useMonospacedFont()
    }

    override fun scrollToBottom() {
        binding.appbar.setExpanded(false)
        binding.scrollViewNoteContent.smoothScrollTo(
            0,
            binding.scrollViewNoteContent.getChildAt(0).height,
            SCROLL_TO_BOTTOM_TIME_MILLISECONDS
        )
    }

    override fun showNoteOptionsDialog(
        isInEditMode: Boolean,
        isLocked: Boolean,
        isArchived: Boolean
    ) {
        val noteOptionsDialog = NoteOptionsDialog.Builder(this).create()

        if (isLocked) noteOptionsDialog.hideLockOption()
        else noteOptionsDialog.hideUnlockOption()

        if (isInEditMode) noteOptionsDialog.showRestoreOption()

        noteOptionsDialog.showArchiveOption()
        noteOptionsDialog.setIsArchived(isArchived)

        noteOptionsDialog.setListener(object: NoteOptionsDialog.Companion.NoteOptionsListener {
            override fun onLockClick() = presenter.handleLockClick()
            override fun onUnlockClick() = presenter.handleUnlockClick()
            override fun onImportClick() = Unit
            override fun onExportClick() = presenter.handleExportClick()
            override fun onOpenClick() = Unit
            override fun onRestoreClick() = presenter.handleRestoreClick()
            override fun onArchiveClick() = presenter.handleArchiveClick()
            override fun onDeleteClick() = presenter.handleDeleteClick()
        })
        noteOptionsDialog.show()
    }

    override fun showColorPickerDialog(color: Int) {
        val dialog = ColorPickerDialogFragment(supportFragmentManager)
        dialog.setTitle("Select Note Color")
        dialog.setButtonText("Select")
        dialog.setInitialCustomColor(color)
        dialog.setColorPickerListener(object : ColorPickerListener {
            override fun onColorSelect(color: Int) = presenter.handleColorChange(color)
        })
        dialog.showDialog()
    }

    override fun showSetNewPasswordDialog() {
        val dialog = PasswordDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Choose Password")
        dialog.setMessage("Once locked, your note will be accessible only by password.")
        dialog.setButtonName("Set")
        dialog.setPasswordDialogListener(object : PasswordDialogListener {
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
    }

    override fun showVerifyNewPasswordDialog(password: String) {
        val dialog = PasswordDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Confirm Password")
        dialog.setMessage("Please enter password again.")
        dialog.setButtonName("Confirm")
        dialog.setPasswordDialogListener(object : PasswordDialogListener {
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
    }

    override fun showNoteLockedMessage() {
        CrystalNoteToast.showShort(this, "Note locked.")
    }

    override fun showRemovePasswordDialog(password: String) {
        val dialog = PasswordDialog.Builder(this).create()
        dialog.setShouldShowErrors(false)
        dialog.show()
        dialog.setTitle("Unlock Note")
        dialog.setMessage("Enter password to unlock note.")
        dialog.setButtonName("Unlock")
        dialog.setPasswordDialogListener(object : PasswordDialogListener {
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
    }

    override fun showNoteUnlockedMessage() {
        CrystalNoteToast.showShort(this, "Note unlocked.")
    }

    override fun showInvalidNameDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Invalid Name")
        dialog.setMessage("Note name is invalid and cannot be saved.")
        dialog.setPositiveButtonName("Discard")
        dialog.setNegativeButtonName("Rename")
        dialog.setListener(object : CrystalNoteDialog.Companion.CrystalNoteDialogListener {
            override fun onPositiveClick() {
                presenter.handleDiscardChangesConfirm()
            }
            override fun onNegativeClick() = Unit
            override fun onBackClick() = Unit
        })
    }

    override fun showDiscardChangesDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Discard Note")
        dialog.setMessage("New note will be permanently deleted. Are you sure?")
        dialog.setPositiveButtonName("Discard")
        dialog.setNegativeButtonName("Cancel")
        dialog.setListener(object : CrystalNoteDialog.Companion.CrystalNoteDialogListener {
            override fun onPositiveClick() {
                presenter.handleDiscardChangesConfirm()
            }
            override fun onNegativeClick() = Unit
            override fun onBackClick() = Unit
        })
    }

    override fun showDeleteNoteDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Delete Note")
        dialog.setMessage("Note will be permanently deleted. Are you sure?")
        dialog.setPositiveButtonName("Confirm")
        dialog.setNegativeButtonName("Cancel")
        dialog.setListener(object : CrystalNoteDialog.Companion.CrystalNoteDialogListener {
            override fun onPositiveClick() {
                presenter.handleDeleteConfirm()
            }
            override fun onNegativeClick() = Unit
            override fun onBackClick() = Unit
        })
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
        startActivityForResult(intent, FILE_CREATE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == FILE_CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                presenter.handleExportFileCreated(uri)
            }
        }
    }

    override fun showExportConfirmationMessage() {
        CrystalNoteToast.showShort(this, "Note exported.")
    }

    override fun showExportErrorMessage() {
        CrystalNoteToast.showLong(this, "Error exporting note.")
    }

    override fun showRestoreDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Restore Note")
        dialog.setMessage("Note will be restored to its last save state, discarding current changes. Are you sure?")
        dialog.setPositiveButtonName("Restore")
        dialog.setNegativeButtonName("Cancel")
        dialog.setListener(object : CrystalNoteDialog.Companion.CrystalNoteDialogListener {
            override fun onPositiveClick() {
                presenter.handleRestoreConfirm()
            }
            override fun onNegativeClick() = Unit
            override fun onBackClick() = Unit
        })
    }

    override fun showRestoreConfirmationMessage() {
        CrystalNoteToast.showLong(this, "Note restored.")
    }

    override fun showNoteArchivedMessage() {
        CrystalNoteToast.showShort(this, "Note archived.")
    }

    override fun showNoteUnarchivedMessage() {
        CrystalNoteToast.showShort(this, "Note unarchived.")
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

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        presenter.handleBackClick()
    }


    /*--- Setup Methods ---*/

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.run {
            isEditMode = true
            setLeftButtonImage(R.drawable.icon_back)
            showRightButton()
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onLeftButtonClick() = presenter.handleBackClick()
                override fun onRightButtonClick() = presenter.handleNoteOptionsClicked()
                override fun onColorClick() = presenter.handleColorClick()
                override fun onTextChange(text: String) = presenter.handleNameTextChange(text)
            })
        }
    }

    private fun setupClickListeners() {
        binding.textNoteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(editable: Editable?) {
                presenter.handleContentTextChange(
                    binding.textNoteContent.text.toString()
                )
            }
        })
        binding.actionButtonBottom.setOnClickListener {
            presenter.handleBottomClick()
        }
    }


    /*--- Constants ---*/

    companion object {
        const val KEY_NOTE_ID = "NOTE_ID_KEY"
        const val KEY_FROM_UPDATE_ACTIVITY = "FROM_UPDATE_ACTIVITY_KEY"
        const val KEY_LAUNCH_FROM_WIDGET = "LAUNCH_FROM_WIDGET_KEY"
        const val KEY_LAUNCH_FROM_SELECT = "LAUNCH_FROM_SELECT_KEY"
        const val KEY_LAUNCH_FROM_UPDATE_FILE = "LAUNCH_FROM_UPDATE_FILE_KEY"
        const val FILE_CREATE_REQUEST_CODE = 264
        const val SCROLL_TO_BOTTOM_TIME_MILLISECONDS = 750
    }
}
