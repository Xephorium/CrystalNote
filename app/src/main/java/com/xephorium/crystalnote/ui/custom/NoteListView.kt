package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.Note
import kotlinx.android.synthetic.main.note_list_layout.view.*

import java.util.Calendar

/*
  NoteListView                                                             05.12.2019
  Christopher Cruzen

    Encapsulating the view behavior of a SwipeRefreshLayout, ScrollView, and two ListViews,
  this class represents a collection of notes organized by reverse chronological order.
  The first ListView is populated by notes modified today and the second is populated by
  notes modified before today.

*/

class NoteListView : SwipeRefreshLayout {


    /*--- Variable Declarations ---*/

    private lateinit var newNotes: MutableList<Note>
    private lateinit var oldNotes: MutableList<Note>

    var noteListViewListener: NoteListViewListener? = null


    /*--- List View Setup ---*/

    constructor(context: Context) : super(context, null) {
        buildNoteListView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        buildNoteListView(context)
    }

    private fun buildNoteListView(context: Context) {
        val layoutInflater = LayoutInflater.from(context)
        val noteListViewLayout = layoutInflater.inflate(R.layout.note_list_layout, null)
        this.addView(noteListViewLayout)

        this.noteListViewListener = getDefaultNoteListViewListener()
        this.setOnRefreshListener(getOnRefreshListener())
    }


    /*--- Public Methods ---*/

    fun populateNoteList(noteList: List<Note>) {
        parseNotes(noteList)

        note_recycler_view.layoutManager = LinearLayoutManager(context)
        note_recycler_view.adapter = getNoteListAdapter(newNotes, oldNotes)
    }


    /*--- Private Utility Methods ---*/

    private fun parseNotes(notes: List<Note>) {
        val newNotes = mutableListOf<Note>()
        val oldNotes = mutableListOf<Note>()

        for (x in notes.indices) {
            if (Calendar.getInstance().time.day == notes[x].date.day)
                newNotes.add(notes[x])
            else
                oldNotes.add(notes[x])
        }

        this.newNotes = newNotes
        this.oldNotes = oldNotes
    }

    private fun getNoteListAdapter(newNotes: List<Note>, oldNotes: List<Note>): NoteListAdapter {
        return object : NoteListAdapter(context, newNotes, oldNotes) {
            override fun getOnClickListener(note: Note): OnClickListener {
                return OnClickListener { noteListViewListener?.onNoteClick(note) }
            }

            override fun getOnLongClickListener(note: Note): OnLongClickListener {
                return OnLongClickListener {
                    noteListViewListener?.onNoteLongClick(note)
                    true
                }
            }
        }
    }

    private fun getDefaultNoteListViewListener() = object : NoteListViewListener {
        override fun onNoteClick(note: Note) {}
        override fun onNoteLongClick(note: Note) {}
        override fun onNoteListRefresh() {}
    }

    private fun getOnRefreshListener() = OnRefreshListener {
        noteListViewListener?.onNoteListRefresh()
        isRefreshing = false
    }


    /*--- Action Handling Interface ---*/

    interface NoteListViewListener {
        fun onNoteClick(note: Note)
        fun onNoteLongClick(note: Note)
        fun onNoteListRefresh()
    }

}
