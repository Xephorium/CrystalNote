package com.xephorium.crystalnote.ui.update

import android.net.Uri
import com.xephorium.crystalnote.data.utility.NoteUtility


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
                initialPreview = it.preview
                date = it.date
                initialColor = it.color
                color = initialColor
                initialPassword = it.password
                password = initialPassword
                initialArchived = it.archived
                archived = initialArchived

                // Update View for Existing Note
                this.view?.populateFields(it.name, it.contents)
                this.view?.populateColor(it.color)
            }

        } else {

            // Update View for New Note
            this.view?.populateColor(color)
        }

        // Update Underline
        if (sharedPreferencesRepository.getNoteUnderlineEnabled())
            this.view?.showTextUnderline()
        else
            this.view?.hideTextUnderline()

        // Update Bottom Button
        val isLongNote = countLinesInNote() > SHOW_BOTTOM_BUTTON_THRESHOLD
        val showBottomButton = sharedPreferencesRepository.getScrollButtonEnabled()
        if (isLongNote && showBottomButton) {
            this.view?.showBottomButton()
        } else {
            this.view?.hideBottomButton()
        }

        // Update Color Orb
        if (sharedPreferencesRepository.getColorOrbEnabled()) this.view?.showColorOrb()
        else this.view?.hideColorOrb()

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

    override fun handleBottomClick() {
        this.view?.scrollToBottom()
    }

    override fun handleColorClick() {
        view?.hideKeyboard()
        view?.showColorPickerDialog(color)
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

            // New Note w/ Name - Save in handleBackground() via onPause()
            returnToCallingScreen()

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

            // Save Existing Note
            saveNote()

        } else if (name.isNotBlank()) {

            // Save New Note
            saveNote()
            isInEditMode = true
        }
    }

    override fun handleNoteOptionsClicked() {
        view?.hideKeyboard()
        view?.showNoteOptionsDialog(isInEditMode, password.isNotEmpty(), archived)
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

    override fun handleRestoreClick() {
        view?.showRestoreDialog()
    }

    override fun handleRestoreConfirm() {

        // Reset State
        name = initialName
        content = initialContent
        color = initialColor
        password = initialPassword

        // Update View
        view?.populateFields(name, content)
        view?.populateColor(color)

        view?.showRestoreConfirmationMessage()
    }

    override fun handleArchiveClick() {
        archived = !archived
        if (archived) {
            view?.showNoteArchivedMessage()
        } else {
            view?.showNoteUnarchivedMessage()
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

    private fun countLinesInNote(): Int {
        return content.lines().size
    }

    private fun saveNote() {

        if (initialName == name
            && initialContent == content
            && initialColor == color
            && initialPassword == password
        ) {

            // No Content Changes - Handle Edge Cases

            if (isOldDatabaseNote()) {

                // If Old Database Note Without Preview - Migrate & Add Preview
                noteRoomRepository.migrateNote(noteId, name, content, date, color, password, archived)

            } else if (initialArchived != archived) {

                // If Archival Updated, Persist Change
                noteRoomRepository.updateNoteArchival(noteId, archived)
            }

        } else if (!isInEditMode) {

            // New Note - Save
            noteId = noteRoomRepository.insertNote(name, content, color, password, archived)

        } else {

            // Existing Note - Update
            noteRoomRepository.updateNote(noteId, name, content, color, password, archived)
        }

        view?.refreshWidget()
    }

    private fun returnToCallingScreen() {
        if (isLaunchFromWidget || isLaunchFromSelect || isLaunchFromUpdateFile) view?.navigateBack()
        else view?.navigateHome()
    }

    private fun isOldDatabaseNote() : Boolean {
        return initialPreview.isEmpty() && NoteUtility.getPreviewFromContents(initialContent).isNotEmpty()
    }
}
