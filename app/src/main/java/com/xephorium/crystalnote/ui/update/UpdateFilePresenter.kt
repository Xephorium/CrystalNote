package com.xephorium.crystalnote.ui.update

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

    override fun handleFileOptionsClick() {
        view?.hideKeyboard()
        view?.showFileOptionsDialog(isFileImported)
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
        if (saveFile()) view?.showFileSavedMessage()
        else view?.showFileAccessDeniedMessage()
        view?.navigateBack()
    }

    override fun handleDestroy() {
        saveFile()
    }


    /*--- Private Initialization Methods ---*/

    private fun initializeStateForLegacyPermissions() {
        if (isFileWriteInitiallyPermitted || isFileWriteGranted) {
            initializeState()
        } else if (!isFileWriteDenied) {
            this.view?.requestFileWritePermission()
        }
    }

    private fun initializeStateForModernPermissions() {
        initializeState()
    }

    private fun initializeState() {
        fileUri?.let {
            noteDiskRepository.readNoteFromTextFile(it).let { note ->
                name = note.name
                initialContent = note.contents
                content = note.contents
            }
            this.view?.populateFields(name, content)
        }
    }


    /*--- Private Save Methods ---*/

    private fun saveFile(): Boolean {
        if (initialContent != content) {
            return noteDiskRepository.writeStringToTextFile(fileUri!!, content)
        }
        return true
    }
}
