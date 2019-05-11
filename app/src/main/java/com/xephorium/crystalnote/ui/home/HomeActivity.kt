package com.xephorium.crystalnote.ui.home

import android.os.Bundle
import android.widget.Toast

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.base.DrawerActivity
import com.xephorium.crystalnote.ui.custom.NoteListView
import com.xephorium.crystalnote.ui.custom.NoteToolbar

import kotlinx.android.synthetic.main.home_activity_layout.*

class HomeActivity : DrawerActivity(), HomeView {

    lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.home_activity_layout)

        presenter = HomePresenter(this)

        home_note_list.noteListViewListener = getHomeNoteListViewListener()
        home_action_button.setOnClickListener { presenter.handleActionButtonClick() }

        setupHomeToolbar()
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
        home_note_list.populateNoteList(notes)
    }

    private fun setupHomeToolbar() {
        toolbar.isEditMode = false
        toolbar.setTitle(R.string.home_title)
        toolbar.setLeftButtonImage(R.drawable.icon_menu)
        toolbar.setRightButtonImage(R.drawable.icon_search)
        toolbar.setNoteToolbarListener(getCreationNoteToolbarListener())
    }

    private fun getCreationNoteToolbarListener() = object : NoteToolbar.NoteToolbarListener {
        override fun onLeftButtonClick() {
            openDrawer()
        }

        override fun onRightButtonClick() {
            Toast.makeText(applicationContext, "Search", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getHomeNoteListViewListener() = object : NoteListView.NoteListViewListener {
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
