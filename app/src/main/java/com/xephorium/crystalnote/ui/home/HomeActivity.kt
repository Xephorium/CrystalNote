package com.xephorium.crystalnote.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.base.DrawerActivity
import com.xephorium.crystalnote.ui.update.UpdateActivity
import com.xephorium.crystalnote.ui.update.UpdateActivity.Companion.KEY_NOTE_NAME
import com.xephorium.crystalnote.ui.custom.NoteListView
import com.xephorium.crystalnote.ui.custom.NoteToolbar

import kotlinx.android.synthetic.main.home_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_activity_layout.*

class HomeActivity : DrawerActivity(), HomeContract.View {


    /*--- Variable Declarations ---*/

    lateinit var presenter: HomePresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.home_activity_layout)

        presenter = HomePresenter()
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


    /*--- View Manipulation Methods ---*/

    override fun populateNoteList(notes: List<Note>) {
        home_note_list.populateNoteList(notes)
    }

    override fun showNavigationDrawer() {
        openDrawer()
    }

    override fun showSearchDialog() {
        // TODO - Handle Search
        Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToEditNote(name: String) {
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra(KEY_NOTE_NAME, name)
        startActivity(intent)
    }

    override fun navigateToNewNote() {
        val intent = Intent(this, UpdateActivity::class.java)
        startActivity(intent)
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        toolbar.isEditMode = false
        toolbar.setTitle(R.string.home_title)
        toolbar.setLeftButtonImage(R.drawable.icon_menu)
        toolbar.setRightButtonImage(R.drawable.icon_search)
        toolbar.setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
            override fun onLeftButtonClick() = presenter.handleMenuButtonClick()
            override fun onRightButtonClick() = presenter.handleSearchButtonClick()
            override fun onTextChange(text: String) = Unit
        })
    }

    private fun setupClickListeners() {
        home_action_button.setOnClickListener { presenter.handleNewNoteButtonClick() }
        home_note_list.noteListViewListener = object : NoteListView.NoteListViewListener {
            override fun onNoteClick(note: Note) = presenter.handleNoteClick(note)
            override fun onNoteLongClick(note: Note) = presenter.handleNoteLongClick(note)
            override fun onNoteListRefresh() = presenter.handleNoteListRefresh()
        }
    }
}
