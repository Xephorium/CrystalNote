package com.xephorium.crystalnote.ui.update

import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.data.validation.NoteValidator

class UpdatePresenter : UpdateContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: UpdateContract.View) {
        super.attachView(view)

        if (isInEditMode) {

            // Get Note
            noteRepository.getNote(noteId)?.let {

                // Update State Fields
                initialName = it.name
                name = initialName
                initialContent = it.contents
                content = initialContent

                // Update View
                this.view?.populateFields(it.name, it.contents)
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
    }


    /*--- Action Handling Methods ---*/

    override fun handleNameTextChange(name: String) {
        this.name = name
    }

    override fun handleContentTextChange(content: String) {
        this.content = content
    }

    override fun handleBackClick() {
        if (!isInEditMode && name.isBlank() && content.isBlank()) {
            returnToCallingScreen()
        } else if (NoteValidator.isValidNoteName(name)) {
            returnToCallingScreen()
        } else {
            view?.showInvalidNameDialog()
        }
    }

    override fun handleBackground() {
        if (NoteValidator.isValidNoteName(name)) {
            saveNote()
        }
    }

    override fun handleDeleteClick() {
        if (isInEditMode) {
            view?.showDeleteNoteDialog()
        } else {
            view?.showDiscardChangesDialog()
        }
    }

    override fun handleDeleteConfirm() {
        noteRepository.deleteNote(noteId)
        if (isLaunchFromWidget) view?.refreshWidget()
        returnToCallingScreen()
    }

    override fun handleDiscardChangesConfirm() {
        returnToCallingScreen()
    }


    /*--- Private Methods ---*/

    private fun saveNote() {

        if (initialName == name && initialContent == content) {

            // No Changes - Do Nothing

        } else if (!isInEditMode) {

            // New Note - Save
            noteRepository.insertNote(name, content, NoteUtility.getDefaultColor())

        } else {

            // Existing Note - Update
            noteRepository.updateNote(noteId, name, content, NoteUtility.getDefaultColor())
        }

        if (isLaunchFromWidget) view?.refreshWidget()
    }

    private fun returnToCallingScreen() {
        if (isLaunchFromWidget) view?.navigateBack()
        else view?.navigateHome()
    }
}
