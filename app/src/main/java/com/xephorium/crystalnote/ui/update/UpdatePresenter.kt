package com.xephorium.crystalnote.ui.update

import com.xephorium.crystalnote.data.validation.NoteValidator

class UpdatePresenter : UpdateContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: UpdateContract.View) {
        super.attachView(view)

        if (isInEditMode) {

            // Get Note'
            noteRepository.getNote(noteId)?.let {

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

        } else if (NoteValidator.isValidNoteName(name) && !isInEditMode) {

            // New Note w/ Valid Name - Save
            returnToCallingScreen()
            saveNote()

        } else if (NoteValidator.isValidNoteName(name)) {

            // Existing Note w/ Valid Name - ???
            returnToCallingScreen()

        } else {
            view?.showInvalidNameDialog()
        }
    }

    override fun handleBackground() {
        if (NoteValidator.isValidNoteName(name) && isInEditMode) {
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

    override fun handleLockClick() {
        // TODO - Properly Handle
        this.password = "password"
        view?.showUnlockMenuOption()
    }

    override fun handleUnlockClick() {
        // TODO - Properly Handle
        this.password = ""
        view?.showLockMenuOption()
    }

    override fun handleDeleteConfirm() {
        noteRepository.deleteNote(noteId)
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
            noteRepository.insertNote(name, content, color, password)

        } else {

            // Existing Note - Update
            noteRepository.updateNote(noteId, name, content, color, password)
        }

        view?.refreshWidget()
    }

    private fun returnToCallingScreen() {
        if (isLaunchFromWidget || isLaunchFromSelect) view?.navigateBack()
        else view?.navigateHome()
    }
}
