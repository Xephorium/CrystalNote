package com.xephorium.crystalnote.ui.update

import android.net.Uri


class UpdateNotePresenter : UpdateNoteContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: UpdateNoteContract.View) {
        super.attachView(view)

        if (isInEditMode) {

            // Get Note
            noteRoomRepository.getNote(noteId)?.let {

                // Populate State Fields for Existing Note
                initialName = it.name
                name = initialName
                initialContent = it.contents
                content = initialContent
                initialColor = it.color
                color = initialColor
                initialPassword = it.password
                password = initialPassword

                // Update View for Existing Note
                this.view?.populateFields(it.name, it.contents)
                this.view?.populateColor(it.color)
            }

        } else {

            // Update View for New Note
            this.view?.populateColor(initialColor)
        }

        // Update Underline
        if (sharedPreferencesRepository.getNoteUnderlineEnabled())
            this.view?.showTextUnderline()
        else
            this.view?.hideTextUnderline()

        // Update Monospaced Font
        if (sharedPreferencesRepository.getMonospacedFontEnabled())
            this.view?.showMonospacedFont()
    }


    /*--- Action Handling Methods ---*/

    override fun handleNameTextChange(name: String) {
        this.name = name
    }

    override fun handleContentTextChange(content: String) {
        this.content = content
    }

    override fun handleColorClick() {
        view?.hideKeyboard()
        view?.showColorPickerDialog()
    }

    override fun handleColorChange(color: Int) {
        this.color = color
        view?.populateColor(this.color)
    }

    override fun handleBackClick() {
        if (!isInEditMode && name.isBlank() && content.isBlank()) {

            // New, Blank Note - Return
            returnToCallingScreen()

        } else if (!isInEditMode && name.isNotBlank()) {

            // New Note w/ Name - Save Here
            returnToCallingScreen()
            saveNote()

        } else if (name.isNotBlank()) {

            // Existing Note w/ Name - Save in handleBackground() via onPause()
            returnToCallingScreen()

        } else {

            // Invalid Name
            view?.showInvalidNameDialog()
        }
    }

    override fun handleBackground() {
        if (name.isNotBlank() && isInEditMode) {
            saveNote()
        }
    }

    override fun handleNoteOptionsClicked() {
        view?.hideKeyboard()
        view?.showNoteOptionsDialog(password.isNotEmpty())
    }

    override fun handleLockClick() {
        view?.hideKeyboard()
        view?.showSetNewPasswordDialog()
    }

    override fun handleNewPasswordSet(password: String) {
        view?.showVerifyNewPasswordDialog(password)
    }

    override fun handleNewPasswordVerify(password: String) {
        this.password = password
        view?.showNoteLockedMessage()
    }

    override fun handleUnlockClick() {
        view?.hideKeyboard()
        view?.showRemovePasswordDialog(password)
    }

    override fun handleOldPasswordVerify() {
        this.password = ""
        view?.showNoteUnlockedMessage()
    }

    override fun handleExportClick() {
        view?.hideKeyboard()
        view?.showExportDialog(noteDiskRepository.getSanitizedExportFileName(name))
    }

    override fun handleExportFileCreated(uri: Uri) {
        if (noteDiskRepository.writeStringToTextFile(uri, content)) {
            view?.showExportConfirmationMessage()
        } else {
            view?.showExportErrorMessage()
        }
    }

    override fun handleDeleteClick() {
        view?.hideKeyboard()
        if (isInEditMode) {
            view?.showDeleteNoteDialog()
        } else {
            view?.showDiscardChangesDialog()
        }
    }

    override fun handleDeleteConfirm() {
        noteRoomRepository.deleteNote(noteId)
        view?.refreshWidget()
        returnToCallingScreen()
    }

    override fun handleDiscardChangesConfirm() {
        returnToCallingScreen()
    }


    /*--- Private Methods ---*/

    private fun saveNote() {

        if (initialName == name
            && initialContent == content
            && initialColor == color
            && initialPassword == password
        ) {

            // No Changes - Do Nothing

        } else if (!isInEditMode) {

            // New Note - Save
            noteRoomRepository.insertNote(name, content, color, password)

        } else {

            // Existing Note - Update
            noteRoomRepository.updateNote(noteId, name, content, color, password)
        }

        view?.refreshWidget()
    }

    private fun returnToCallingScreen() {
        if (isLaunchFromWidget || isLaunchFromSelect || isLaunchFromUpdateFile) view?.navigateBack()
        else view?.navigateHome()
    }
}
