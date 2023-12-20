package com.xephorium.crystalnote.ui.update

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.utility.NoteUtility


class UpdateFilePresenter : UpdateFileContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: UpdateFileContract.View) {
        super.attachView(view)

        if (fileUri == null) {
            this.view?.showErrorReadingFileMessage()
            this.view?.navigateBack()
        }

        // Update View
        if (isFirstLaunch) {
            if (isLegacyBuild) initializeStateForLegacyPermissions()
            else initializeStateForModernPermissions()
        }

        // Update Bottom Button
        if (countLinesInNote() > UpdateNoteContract.Presenter.SHOW_BOTTOM_BUTTON_THRESHOLD)
            this.view?.showBottomButton()
        else
            this.view?.hideBottomButton()

        // Update Underline
        if (sharedPreferencesRepository.getNoteUnderlineEnabled()) this.view?.showTextUnderline()
        else this.view?.hideTextUnderline()

        // Update Monospaced Font
        if (sharedPreferencesRepository.getMonospacedFontEnabled()) this.view?.showMonospacedFont()
    }


    /*--- Action Handling Methods ---*/

    override fun handleContentTextChange(content: String) {
        this.content = content
    }

    override fun handleFileWritePermissionGranted() {
        isFileWriteGranted = true
    }

    override fun handleFileWritePermissionDenied() {
        isFileWriteDenied = true
        view?.navigateBack()
    }

    override fun handleFileWritePermissionDeniedConfirm() {
        view?.navigateBack()
    }

    override fun handleBottomClick() {
        this.view?.scrollToBottom()
    }

    override fun handleFileOptionsClick() {
        view?.hideKeyboard()
        view?.showFileOptionsDialog(isFileImported, isLegacyBuild)
    }

    override fun handleRestoreClick() {
        view?.hideKeyboard()
        view?.showRestoreDialog()
    }

    override fun handleRestoreConfirm() {
        initialContent?.let {
            content = it
            view?.populateFields(name, content)
        }
    }

    override fun handleImportClick() {
        view?.hideKeyboard()
        view?.showImportDialog()
    }

    override fun handleImportConfirm() {
        newNoteId = noteRoomRepository.insertNote(name, content, NoteUtility.getDefaultColor(), "")
        this.view?.showImportSuccessMessage()
    }

    override fun handleOpenNoteClick() {
        view?.navigateToUpdateNote(newNoteId)
    }

    override fun handleBackClick() {
        if (isLegacyBuild && isContentChanged) {
            if (saveFile()) view?.showFileSavedMessage()
            else view?.showFileAccessDeniedMessage()
        }

        view?.navigateBack()
    }

    override fun handleDestroy() {
        if (isLegacyBuild && isContentChanged) saveFile()
    }


    /*--- Private Methods ---*/

    private fun countLinesInNote(): Int {
        return content.lines().size
    }

    private fun initializeStateForLegacyPermissions() {
        if (isFileWriteInitiallyPermitted || isFileWriteGranted) {
            initializeState()
        } else if (!isFileWriteDenied) {
            this.view?.requestFileWritePermission()
        }
    }

    private fun initializeStateForModernPermissions() {
        /* Note: Storage access permissions for Android API > 29 make editing
         *       files opened from the file manager a prohibitively clunky
         *       experience. Since it's a comparatively minor feature, I've
         *       simply disabled plaintext editing on newer versions of Android.
         *       For more info, see my full writeup in 'DevLog.txt'.
         */
        initializeState()
        view?.disableFileEdit()
    }

    private fun initializeState() {
        fileUri?.let {
            noteDiskRepository.readNoteFromTextFile(it).let { note ->
                name = note.name
                initialContent = note.contents
                content = note.contents ?: Note.NOTE_ERROR_CONTENT
            }
            this.view?.populateFields(name, content)
        }
    }


    /*--- Private Save Methods ---*/

    private fun saveFile(): Boolean {
        return fileUri?.let { noteDiskRepository.writeStringToTextFile(it, content) } ?: false
    }
}
