package com.xephorium.crystalnote.ui.update

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.CrystalNoteToast
import com.xephorium.crystalnote.databinding.UpdateActivityLayoutBinding
import com.xephorium.crystalnote.ui.base.BaseActivity
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity.Companion.KEY_LAUNCH_FROM_UPDATE_FILE
import com.xephorium.crystalnote.ui.utility.KeyboardUtility


class UpdateFileActivity() : BaseActivity(), UpdateFileContract.View {


    /*--- Variable Declarations ---*/

    private val fileUri: Uri?
        get() = intent.data

    private lateinit var updateBinding: UpdateActivityLayoutBinding

    lateinit var presenter: UpdateFilePresenter

    private lateinit var optionsMenu: Menu


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = UpdateActivityLayoutBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.file_toolbar_options, menu)
        optionsMenu = menu
        optionsMenu.findItem(R.id.option_open_note).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_revert -> presenter.handleRevertClick()
            R.id.option_import -> presenter.handleImportClick()
            R.id.option_open_note -> presenter.handleOpenNoteClick()
        }
        return super.onOptionsItemSelected(item)
    }


    /*--- View Manipulation Methods ---*/

    override fun populateFields(name: String, content: String) {
        updateBinding.toolbar.setTitle(name)
        updateBinding.textNoteContent.setText(content)
    }

    override fun showTextUnderline() {
        updateBinding.textNoteContent.showUnderline()
    }

    override fun hideTextUnderline() {
        updateBinding.textNoteContent.hideUnderline()
    }

    override fun showMonospacedFont() {
        updateBinding.textNoteContent.useMonospacedFont()
    }

    override fun showRevertDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(true)
        alertDialog.setTitle("Revert Changes")
        alertDialog.setMessage("Restore file to its initial state?")
        alertDialog.setButton(BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(BUTTON_POSITIVE, "Yes") { dialog, _ ->
            dialog.dismiss()
            presenter.handleRevertConfirm()
        }
        alertDialog.show()
    }

    override fun showImportDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(true)
        alertDialog.setTitle("Import File")
        alertDialog.setMessage("Save file contents as new note?")
        alertDialog.setButton(BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(BUTTON_POSITIVE, "Yes") { dialog, _ ->
            dialog.dismiss()
            presenter.handleImportConfirm()
        }
        alertDialog.show()
    }

    override fun showImportSuccessMessage() {
        CrystalNoteToast.showLong(this, "File imported. Tap 'Open Note' to view.")
    }

    override fun showOpenNoteMenuOption() {
        optionsMenu.findItem(R.id.option_import).isVisible = false
        optionsMenu.findItem(R.id.option_open_note).isVisible = true
    }

    override fun showFileSavedMessage() {
        Toast.makeText(this, "File saved.", Toast.LENGTH_SHORT).show()
    }

    override fun showFileAccessDeniedMessage() {
        Toast.makeText(
            this,
            "Error saving: file access denied.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showErrorReadingFileMessage() {
        Toast.makeText(this, "Error reading file.", Toast.LENGTH_SHORT).show()
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
        setSupportActionBar(updateBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        updateBinding.toolbar.run {
            isEditMode = false
            setLeftButtonImage(NoteToolbar.NO_IMAGE)
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onLeftButtonClick() = Unit
                override fun onRightButtonClick() = Unit
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

    private fun setupClickListeners() {
        updateBinding.textNoteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(editable: Editable?) {
                presenter.handleContentTextChange(updateBinding.textNoteContent.text.toString())
            }
        })
    }


    /*--- Constants ---*/

    companion object {
        const val FILE_WRITE_REQUEST_CODE = 117
    }
}
