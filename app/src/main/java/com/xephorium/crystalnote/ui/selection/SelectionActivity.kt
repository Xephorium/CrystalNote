package com.xephorium.crystalnote.ui.selection

import android.os.Bundle
import android.widget.Toast

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.base.ToolbarActivity
import com.xephorium.crystalnote.ui.custom.NoteListView
import com.xephorium.crystalnote.ui.custom.NoteToolbar

import kotlinx.android.synthetic.main.selection_activity_layout.*

class SelectionActivity : ToolbarActivity(), SelectionView {

    lateinit var presenter: SelectionPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.selection_activity_layout)

        presenter = SelectionPresenter(this)

        selection_note_list.noteListViewListener = getSelectionNoteListViewListener()
        selection_action_button.setOnClickListener { presenter.handleActionButtonClick() }

        setupSelectionToolbar()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.refreshNoteList()
    }

    public override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun populateNoteList(notes: List<Note>) {
        selection_note_list.populateNoteList(notes)
    }

    override fun closeSelectionActivity() {
        this.finish()
    }

    private fun setupSelectionToolbar() {
        toolbar.isEditMode = false
        toolbar.setTitle(R.string.selection_title)
        toolbar.setLeftButtonImage(R.drawable.icon_back)
        toolbar.setRightButtonImage(R.drawable.icon_search)
        toolbar.setNoteToolbarListener(getSelectionNoteToolbarListener())
    }

    private fun getSelectionNoteToolbarListener() = object : NoteToolbar.NoteToolbarListener {
        override fun onLeftButtonClick() {
            presenter.handleToolbarBackClick()
        }

        override fun onRightButtonClick() {
            Toast.makeText(applicationContext, "Search Notes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSelectionNoteListViewListener() = object : NoteListView.NoteListViewListener {
        override fun onNoteClick(note: Note) {
            presenter.handleNoteClick(note)
        }

        override fun onNoteLongClick(note: Note): Boolean {
            return presenter.handleNoteLongClick(note)
        }

        override fun onNoteListRefresh() {
            presenter.refreshNoteList()
        }
    }
}
