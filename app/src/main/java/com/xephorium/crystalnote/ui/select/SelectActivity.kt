package com.xephorium.crystalnote.ui.select

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.data.SharedPreferencesRepository
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.base.ToolbarActivity
import com.xephorium.crystalnote.ui.update.UpdateActivity
import com.xephorium.crystalnote.ui.custom.NoteListView
import com.xephorium.crystalnote.ui.custom.NoteToolbar

import kotlinx.android.synthetic.main.select_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_activity_layout.*
import com.xephorium.crystalnote.ui.widget.NotesWidgetProvider

class SelectActivity : ToolbarActivity(), SelectContract.View {


    /*--- Variable Declarations ---*/

    lateinit var presenter: SelectPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.select_activity_layout)

        presenter = SelectPresenter()
        presenter.noteRepository = NoteRepository(this)
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)

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


    /*--- View Manipulation Methods ---*/

    override fun populateNoteList(notes: List<Note>) {
        listSelectNotes.visibility = View.VISIBLE
        textSelectEmpty.visibility = View.GONE
        listSelectNotes.populateNoteList(notes)
    }

    override fun showEmptyNotesList() {
        listSelectNotes.visibility = View.GONE
        textSelectEmpty.visibility = View.VISIBLE
    }

    override fun refreshWidget() {
        NotesWidgetProvider.refreshWidgets(this, application)
    }

    override fun navigateBack() {
        finish()
    }

    override fun navigateToNewNote() {
        val intent = Intent(this, UpdateActivity::class.java)
        startActivity(intent)
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        toolbar.isEditMode = false
        toolbar.setTitle(R.string.selectTitle)
        toolbar.setLeftButtonImage(R.drawable.icon_back)
        toolbar.setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
            override fun onLeftButtonClick() = presenter.handleToolbarBackClick()
            override fun onRightButtonClick() = Unit
            override fun onTextChange(text: String) = Unit
        })
    }

    private fun setupClickListeners() {
        floatingActionButtonSelect.setOnClickListener { presenter.handleNewNoteButtonClick() }
        listSelectNotes.noteListViewListener = object : NoteListView.NoteListViewListener {
            override fun onNoteClick(note: Note) = presenter.handleNoteClick(note)
            override fun onNoteLongClick(note: Note) = presenter.handleNoteLongClick(note)
            override fun onNoteListRefresh() = presenter.handleNoteListRefresh()
        }
    }
}
