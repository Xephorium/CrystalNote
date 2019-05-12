package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.Note
import kotlinx.android.synthetic.main.note_list_view_layout.view.*

import java.util.ArrayList
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
        val noteListViewLayout = layoutInflater.inflate(R.layout.note_list_view_layout, null)
        this.addView(noteListViewLayout)

        this.noteListViewListener = getDefaultNoteListViewListener()
        this.setOnRefreshListener(getOnRefreshListener())
    }


    /*--- Public Methods ---*/

    fun populateNoteList(noteList: List<Note>) {
        parseNotes(noteList)

        if (newNotes.isNotEmpty() && oldNotes.isNotEmpty()) {
            new_note_header.visibility = View.VISIBLE
            new_note_list_view.visibility = View.VISIBLE
            new_note_list_view.adapter = getNoteListAdapter(newNotes)
            old_note_header.visibility = View.VISIBLE
            old_note_list_view.visibility = View.VISIBLE
            old_note_list_view.adapter = getNoteListAdapter(oldNotes)
        } else if (newNotes.isNotEmpty()) {
            new_note_header.visibility = View.GONE
            new_note_list_view.visibility = View.VISIBLE
            new_note_list_view.adapter = getNoteListAdapter(newNotes)
            old_note_header.visibility = View.GONE
            old_note_list_view.visibility = View.GONE
            old_note_list_view.adapter = getNoteListAdapter(ArrayList())
        } else if (oldNotes.isNotEmpty()) {
            new_note_header.visibility = View.GONE
            new_note_list_view.visibility = View.GONE
            new_note_list_view.adapter = getNoteListAdapter(ArrayList())
            old_note_header.visibility = View.GONE
            old_note_list_view.visibility = View.VISIBLE
            old_note_list_view.adapter = getNoteListAdapter(oldNotes)
        } else {
            new_note_header.visibility = View.GONE
            new_note_list_view.visibility = View.GONE
            new_note_list_view.adapter = getNoteListAdapter(ArrayList())
            old_note_header.visibility = View.GONE
            old_note_list_view.visibility = View.GONE
            old_note_list_view.adapter = getNoteListAdapter(ArrayList())
        }
        new_note_list_view.layoutParams = getScrollViewHeightParams(new_note_list_view)
        old_note_list_view.layoutParams = getScrollViewHeightParams(old_note_list_view)
    }


    /*--- Private Utility Methods ---*/

    private fun parseNotes(notes: List<Note>) {
        val newNotes = mutableListOf<Note>()
        val oldNotes = mutableListOf<Note>()

        for (x in notes.indices) {
            if (Calendar.getInstance().time.day == notes[x].date!!.day)
                newNotes.add(notes[x])
            else
                oldNotes.add(notes[x])
        }

        this.newNotes = newNotes
        this.oldNotes = oldNotes
    }

    private fun getNoteListAdapter(notes: List<Note>): NoteListAdapter {
        return object : NoteListAdapter(notes) {
            override fun getOnClickListener(position: Int): OnClickListener {
                return OnClickListener { noteListViewListener?.onNoteClick(notes[position]) }
            }

            override fun getOnLongClickListener(position: Int): OnLongClickListener {
                return OnLongClickListener {
                    noteListViewListener?.onNoteLongClick(notes[position])
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

    private fun getScrollViewHeightParams(listView: ListView): LayoutParams {
        var listViewHeight = 0
        for (x in 0 until listView.adapter.count) {
            val listItem = listView.adapter.getView(x, null, listView)
            listItem.measure(0, 0)
            listViewHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = listViewHeight + listView.dividerHeight * (listView.adapter.count - 1)
        return params
    }


    /*--- Action Handling Interface ---*/

    interface NoteListViewListener {
        fun onNoteClick(note: Note)
        fun onNoteLongClick(note: Note)
        fun onNoteListRefresh()
    }

}
