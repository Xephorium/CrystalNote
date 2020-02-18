package com.xephorium.crystalnote.ui.update

import com.xephorium.crystalnote.data.validation.NoteValidator

class UpdatePresenter : UpdateContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: UpdateContract.View) {
        super.attachView(view)

        if (isInEditMode) {

            // Get Note
            noteRepository.getNote(initialName)?.let {

                // Update State Fields
                name = initialName
                initialContent = noteRepository.readNoteContents(it.name)
                content = initialContent

                // Update View
                this.view?.populateFields(it.name, noteRepository.readNoteContents(it.name))
            }
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
            view?.navigateBack()
        } else if (NoteValidator.isValidNoteName(name)) {
            saveNote()
            view?.navigateBack()
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
        noteRepository.deleteNote(initialName)
        if (isLaunchFromWidget) view?.refreshWidget()
        view?.navigateBack()
    }

    override fun handleDiscardChangesConfirm() {
        view?.navigateBack()
    }


    /*--- Private Methods ---*/

    // TODO - Distinguish between updating the current note and saving over another. (Primary Key?)
    private fun saveNote() {

        if (initialName == name && initialContent == content) {

            // No Changes - Do Nothing

        } else if (initialName == name) {

            // Note Updated - Save Over Existing
            noteRepository.writeToNote(name, content)

        } else if (isInEditMode && initialName != name) {

            // Note Renamed - Save New, Delete Old
            noteRepository.writeToNote(name, content)
            noteRepository.deleteNote(initialName)

        } else if (!isInEditMode) {

            // New Note - Save
            noteRepository.writeToNote(name, content)
        }

        if (isLaunchFromWidget) view?.refreshWidget()
    }
}
