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
        if (initialContent.isEmpty()) {
            if (isFileWriteInitiallyPermitted || isFileWriteGranted) {
                noteDiskRepository.readPlaintextFile(fileUri!!).let { note ->
                    name = note.name
                    initialContent = note.contents
                    content = initialContent
                }
                this.view?.populateFields(name, initialContent)
            } else if (!isFileWriteDenied) {
                this.view?.requestFileWritePermission()
            }
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

    override fun handleRevertClick() {
        view?.hideKeyboard()
        view?.showRevertDialog()
    }

    override fun handleRevertConfirm() {
        content = initialContent
        view?.populateFields(name, initialContent)
    }

    override fun handleImportClick() {
        view?.hideKeyboard()
        view?.showImportDialog()
    }

    override fun handleImportConfirm() {
        newNoteId = noteRoomRepository.insertNote(name, content, NoteUtility.getDefaultColor(), "")
        this.view?.showImportSuccessMessage()
        this.view?.showOpenNoteMenuOption()
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


    /*--- Private Methods ---*/

    private fun saveFile(): Boolean {
        if (initialContent != content) {
            return noteDiskRepository.writePlaintextFile(fileUri!!, content)
        }
        return true
    }
}
