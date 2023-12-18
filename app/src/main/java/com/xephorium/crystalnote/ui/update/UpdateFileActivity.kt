package com.xephorium.crystalnote.ui.update

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.CrystalNoteToast
import com.xephorium.crystalnote.databinding.UpdateActivityLayoutBinding
import com.xephorium.crystalnote.ui.base.BaseActivity
import com.xephorium.crystalnote.ui.custom.CrystalNoteDialog
import com.xephorium.crystalnote.ui.custom.NoteOptionsDialog
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity.Companion.KEY_LAUNCH_FROM_UPDATE_FILE
import com.xephorium.crystalnote.ui.utility.KeyboardUtility


class UpdateFileActivity() : BaseActivity(), UpdateFileContract.View {


    /*--- Variable Declarations ---*/

    private val fileUri: Uri?
        get() = intent.data

    private lateinit var binding: UpdateActivityLayoutBinding

    lateinit var presenter: UpdateFilePresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UpdateActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = UpdateFilePresenter()
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)
        presenter.noteRoomRepository = NoteRoomRepository(this)
        presenter.noteDiskRepository = NoteDiskRepository(this)
        presenter.fileUri = fileUri
        presenter.isFileWriteInitiallyPermitted = checkFileWritePermission()

        setupToolbar()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    public override fun onDestroy() {
        super.onDestroy()
        presenter.handleDestroy()
        presenter.detachView()
    }


    /*--- View Manipulation Methods ---*/

    override fun populateFields(name: String, content: String) {
        binding.toolbar.setTitle(name)
        binding.textNoteContent.setText(content)
    }

    override fun showBottomButton() {
        binding.actionButtonBottom.visibility = View.VISIBLE
    }

    override fun hideBottomButton() {
        binding.actionButtonBottom.visibility = View.GONE
    }

    override fun disableFileEdit() {
        binding.textNoteContent.isEnabled = false
    }

    override fun showTextUnderline() {
        binding.textNoteContent.showUnderline()
    }

    override fun hideTextUnderline() {
        binding.textNoteContent.hideUnderline()
    }

    override fun showMonospacedFont() {
        binding.textNoteContent.useMonospacedFont()
    }

    override fun scrollToBottom() {
        binding.appbar.setExpanded(false)
        binding.scrollViewNoteContent.smoothScrollTo(
            0,
            binding.scrollViewNoteContent.getChildAt(0).height,
            UpdateNoteActivity.SCROLL_TO_BOTTOM_TIME_MILLISECONDS
        )
    }

    override fun showFileOptionsDialog(isFileImported: Boolean, isLegacyBuild: Boolean) {
        val dialog = NoteOptionsDialog.Builder(this).create()

        dialog.setTitle("File Options")

        dialog.hideLockOption()
        dialog.hideUnlockOption()
        dialog.hideExportOption()
        if (!isFileImported) dialog.showImportOption()
        if (isFileImported) dialog.showOpenOption()
        if (isLegacyBuild) dialog.showRestoreOption()
        dialog.hideDeleteOption()

        dialog.setListener(object: NoteOptionsDialog.Companion.NoteOptionsListener {
            override fun onLockClick() = Unit
            override fun onUnlockClick() = Unit
            override fun onImportClick() = presenter.handleImportClick()
            override fun onExportClick() = Unit
            override fun onOpenClick() = presenter.handleOpenNoteClick()
            override fun onRestoreClick() = presenter.handleRestoreClick()
            override fun onDeleteClick() = Unit
        })
        dialog.show()
    }

    override fun showRestoreDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Restore File")
        dialog.setMessage("File will be restored to its last save state, discarding current changes. Are you sure?")
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

    override fun showImportDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Import File")
        dialog.setMessage("Save file contents as new note?")
        dialog.setPositiveButtonName("Import")
        dialog.setNegativeButtonName("Cancel")
        dialog.setListener(object : CrystalNoteDialog.Companion.CrystalNoteDialogListener {
            override fun onPositiveClick() {
                presenter.handleImportConfirm()
            }
            override fun onNegativeClick() = Unit
            override fun onBackClick() = Unit
        })
    }

    override fun showImportSuccessMessage() {
        CrystalNoteToast.showLong(this, "File imported. Tap 'Open Note' to edit.")
    }

    override fun showFileSavedMessage() {
        CrystalNoteToast.showShort(this, "File saved.")
    }

    override fun showFileAccessDeniedMessage() {
        CrystalNoteToast.showLong(this, "Error saving: file access denied.")
    }

    override fun showErrorReadingFileMessage() {
        CrystalNoteToast.showShort(this, "Error reading file.")
    }

    override fun navigateBack() {
        if (isTaskRoot) finish()
        else finishAffinity()
    }

    override fun navigateToUpdateNote(id: Int) {
        val intent = Intent(this, UpdateNoteActivity::class.java)
        intent.putExtra(UpdateNoteActivity.KEY_NOTE_ID, id)
        intent.putExtra(KEY_LAUNCH_FROM_UPDATE_FILE, true)
        startActivity(intent)
        finish()
    }

    override fun hideKeyboard() {
        KeyboardUtility.hideKeyboard(this)
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        presenter.handleBackClick()
    }


    /*--- Permissions Methods ---*/

    override fun requestFileWritePermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                FILE_WRITE_REQUEST_CODE
        )
    }

    @SuppressLint("MissingSuperCall")
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

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.run {
            isEditMode = false
            setLeftButtonImage(NoteToolbar.NO_IMAGE)
            showRightButton()
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onLeftButtonClick() = Unit
                override fun onRightButtonClick() = presenter.handleFileOptionsClick()
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

    private fun setupClickListeners() {
        binding.textNoteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(editable: Editable?) {
                presenter.handleContentTextChange(binding.textNoteContent.text.toString())
            }
        })
        binding.actionButtonBottom.setOnClickListener {
            presenter.handleBottomClick()
        }
    }


    /*--- Constants ---*/

    companion object {
        const val FILE_WRITE_REQUEST_CODE = 117
    }
}
