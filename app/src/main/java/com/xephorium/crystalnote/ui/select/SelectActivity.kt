package com.xephorium.crystalnote.ui.select

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.databinding.SelectActivityLayoutBinding
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

    private lateinit var selectBinding: SelectActivityLayoutBinding


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectBinding = SelectActivityLayoutBinding.inflate(layoutInflater)
        setBoundViewAsContent(selectBinding)

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

    override fun populateNoteList(notes: List<PreviewNote>) {
        selectBinding.run {
            listSelectNotes.visibility = View.VISIBLE
            textSelectEmpty.visibility = View.GONE
            listSelectNotes.populateNoteList(notes, true)
        }
    }

    override fun showEmptyNotesList() {
        selectBinding.run {
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
        toolbarBinding.toolbar.run {
            isEditMode = false
            setTitle(R.string.selectTitle)
            setLeftButtonImage(R.drawable.icon_back)
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onLeftButtonClick() = presenter.handleToolbarBackClick()
                override fun onRightButtonClick() = Unit
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

    private fun setupClickListeners() {
        selectBinding.run {
            floatingActionButtonSelect.setOnClickListener { presenter.handleNewNoteButtonClick() }
            listSelectNotes.noteListViewListener = object : NoteListView.NoteListViewListener {
                override fun onNoteClick(note: PreviewNote) = presenter.handleNoteClick(note)
                override fun onNoteLongClick(note: PreviewNote) = presenter.handleNoteLongClick(note)
                override fun onNoteListRefresh() = presenter.handleNoteListRefresh()
            }
        }
    }
}
