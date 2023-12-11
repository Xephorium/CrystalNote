package com.xephorium.crystalnote.ui.select

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.databinding.SelectActivityLayoutBinding
import com.xephorium.crystalnote.databinding.ToolbarActivityLayoutBinding
import com.xephorium.crystalnote.ui.base.ToolbarActivity
import com.xephorium.crystalnote.ui.custom.NoteListView
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity.Companion.KEY_LAUNCH_FROM_SELECT
import com.xephorium.crystalnote.ui.widget.NotesWidgetProvider
import com.xephorium.crystalnote.ui.widget.NotesWidgetProvider.Companion.KEY_WIDGET_ID

class SelectActivity : ToolbarActivity(), SelectContract.View {


    /*--- Variable Declarations ---*/

    private val widgetId: Int
        get() = intent.getIntExtra(KEY_WIDGET_ID, 0)

    lateinit var presenter: SelectPresenter

    private lateinit var selectActivityBinding: SelectActivityLayoutBinding

    private lateinit var toolbarBinding: ToolbarActivityLayoutBinding


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectActivityBinding = SelectActivityLayoutBinding.inflate(layoutInflater)
        toolbarBinding = ToolbarActivityLayoutBinding.inflate(layoutInflater)
        setActivityContent(R.layout.select_activity_layout)

        presenter = SelectPresenter()
        presenter.noteRepository = NoteRoomRepository(this)
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)
        presenter.widgetId = widgetId

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
        selectActivityBinding.run {
            listSelectNotes.visibility = View.VISIBLE
            textSelectEmpty.visibility = View.GONE
            listSelectNotes.populateNoteList(notes)
        }
    }

    override fun showEmptyNotesList() {
        selectActivityBinding.run {
            listSelectNotes.visibility = View.GONE
            textSelectEmpty.visibility = View.VISIBLE
        }
    }

    override fun refreshWidget() {
        NotesWidgetProvider.refreshWidgets(this, application)
    }

    override fun navigateBack() {
        finish()
    }

    override fun navigateToNewNote() {
        val intent = Intent(this, UpdateNoteActivity::class.java)
        intent.putExtra(KEY_LAUNCH_FROM_SELECT, true)
        startActivity(intent)
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        toolbarBinding.run {
            toolbar.isEditMode = false
            toolbar.setTitle(R.string.selectTitle)
            toolbar.setLeftButtonImage(R.drawable.icon_back)
            toolbar.setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onButtonClick() = presenter.handleToolbarBackClick()
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

    private fun setupClickListeners() {
        selectActivityBinding.run {
            floatingActionButtonSelect.setOnClickListener { presenter.handleNewNoteButtonClick() }
            listSelectNotes.noteListViewListener = object : NoteListView.NoteListViewListener {
                override fun onNoteClick(note: Note) = presenter.handleNoteClick(note)
                override fun onNoteLongClick(note: Note) = presenter.handleNoteLongClick(note)
                override fun onNoteListRefresh() = presenter.handleNoteListRefresh()
            }
        }
    }
}
