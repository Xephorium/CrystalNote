package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.databinding.NoteListLayoutBinding

/*
  NoteListView                                                             05.12.2019
  Christopher Cruzen

    Encapsulating the view behavior of a SwipeRefreshLayout and a custom RecyclerView,
  this class represents a collection of notes organized by reverse chronological order.
  The first section is populated by notes modified today and the second is populated by
  notes modified before today.

*/

class NoteListView : SwipeRefreshLayout {


    /*--- Variable Declarations ---*/

    private lateinit var newNotes: MutableList<Note>
    private lateinit var oldNotes: MutableList<Note>

    var noteListViewListener: NoteListViewListener? = null

    private lateinit var binding: NoteListLayoutBinding


    /*--- List View Setup ---*/

    constructor(context: Context) : super(context, null) {
        buildNoteListView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        buildNoteListView(context)
    }

    private fun buildNoteListView(context: Context) {
        binding = NoteListLayoutBinding.inflate(LayoutInflater.from(context))
        this.addView(binding.root)

        this.noteListViewListener = getDefaultNoteListViewListener()
        this.setOnRefreshListener(getOnRefreshListener())
    }


    /*--- Public Methods ---*/

    fun populateNoteList(noteList: List<Note>) {
        parseNotes(noteList)

        binding.listNotes.layoutManager = LinearLayoutManager(context)
        binding.listNotes.adapter = getNoteListAdapter(newNotes, oldNotes)
    }


    /*--- Private Utility Methods ---*/

    private fun parseNotes(notes: List<Note>) {
        val newNotes = mutableListOf<Note>()
        val oldNotes = mutableListOf<Note>()

        for (x in notes.indices) {
            if (DateUtils.isToday(notes[x].date.time))
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
