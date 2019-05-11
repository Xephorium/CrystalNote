package com.xephorium.crystalnote.ui.creation

import android.os.Bundle
import android.text.TextUtils

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.base.ToolbarActivity
import com.xephorium.crystalnote.ui.custom.NoteToolbar

import kotlinx.android.synthetic.main.creation_activity_layout.*

class CreationActivity : ToolbarActivity(), CreationView {

    lateinit var presenter: CreationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.creation_activity_layout)

        presenter = CreationPresenter(this)
        setupCreationToolbar()
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

    override fun closeCreationActivity() {
        this.finish()
    }

    override fun isNewNotePopulated(): Boolean {
        return creation_content.text?.let {
            !TextUtils.isEmpty(toolbar.titleContent) || !TextUtils.isEmpty(it)
        } ?: false
    }

    private fun setupCreationToolbar() {
        toolbar.isEditMode = true
        toolbar.setLeftButtonImage(R.drawable.icon_back)
        toolbar.setRightButtonImage(R.drawable.icon_save)
        toolbar.setNoteToolbarListener(getCreationNoteToolbarListener())
    }

    private fun getCreationNoteToolbarListener() = object : NoteToolbar.NoteToolbarListener {
        override fun onLeftButtonClick() {
            presenter.handleBackClick()
        }

        override fun onRightButtonClick() {
            creation_content.text?.let {
                presenter.handleSaveClick(toolbar.titleContent, it.toString())
            }
        }
    }
}
