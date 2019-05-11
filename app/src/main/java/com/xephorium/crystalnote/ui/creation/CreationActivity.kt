package com.xephorium.crystalnote.ui.creation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.ui.base.ToolbarActivity
import com.xephorium.crystalnote.ui.custom.NoteToolbar

import kotlinx.android.synthetic.main.creation_activity_layout.*

class CreationActivity : ToolbarActivity(), CreationContract.View {


    /*--- Variable Declarations ---*/

    lateinit var presenter: CreationPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.creation_activity_layout)

        presenter = CreationPresenter()
        presenter.noteRepository = NoteRepository(this)

        setupToolbar()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    public override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onBackPressed() {
        presenter.handleBackClick()
    }


    /*--- View Manipulation Methods ---*/

    override fun showDraftDiscardedMessage() {
        Toast.makeText(this, "Draft Discarded", Toast.LENGTH_SHORT).show()
    }

    override fun showInvalidNoteMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateBack() {
        finish()
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        toolbar.isEditMode = true
        toolbar.setLeftButtonImage(R.drawable.icon_back)
        toolbar.setRightButtonImage(R.drawable.icon_save)
        toolbar.setNoteToolbarListener( object : NoteToolbar.NoteToolbarListener {
            override fun onLeftButtonClick() = presenter.handleBackClick()
            override fun onRightButtonClick() = presenter.handleSaveClick()
            override fun onTextChange(text: String) = presenter.handleNameTextChange(text)
        })
    }

    private fun setupClickListeners() {
        creation_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(editable: Editable?) {
                presenter.handleContentTextChange(creation_content.text.toString())
            }
        })
    }
}
