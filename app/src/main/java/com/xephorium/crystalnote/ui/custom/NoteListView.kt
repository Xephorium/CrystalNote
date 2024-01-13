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
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.databinding.NoteListLayoutBinding

/*
  NoteListView                                                             05.12.2019
  Christopher Cruzen

    Encapsulating the view behavior of a SwipeRefreshLayout and a custom RecyclerView,
  this class represents a collection of PreviewNotes organized by reverse chronological
  order. The first section is populated by PreviewNotes modified today and the second
  is populated by PreviewNotes modified before today.

*/

class NoteListView : SwipeRefreshLayout {


    /*--- Variable Declarations ---*/

    private lateinit var newNotes: MutableList<PreviewNote>
    private lateinit var oldNotes: MutableList<PreviewNote>

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

    fun populateNoteList(noteList: List<PreviewNote>, showArchived: Boolean) {
        parseNotes(noteList)

        binding.listNotes.layoutManager = LinearLayoutManager(context)
        binding.listNotes.adapter = getNoteListAdapter(newNotes, oldNotes, showArchived)
    }


    /*--- Private Utility Methods ---*/

    private fun parseNotes(notes: List<PreviewNote>) {
        val newNotes = mutableListOf<PreviewNote>()
        val oldNotes = mutableListOf<PreviewNote>()

        for (x in notes.indices) {
            if (DateUtils.isToday(notes[x].date.time))
                newNotes.add(notes[x])
            else
                oldNotes.add(notes[x])
        }

        this.newNotes = newNotes
        this.oldNotes = oldNotes
    }

    private fun getNoteListAdapter(
        newNotes: List<PreviewNote>,
        oldNotes: List<PreviewNote>,
        showArchived: Boolean
    ): NoteListAdapter {
        return object : NoteListAdapter(context, newNotes, oldNotes, showArchived) {
            override fun getOnClickListener(note: PreviewNote): OnClickListener {
                return OnClickListener { noteListViewListener?.onNoteClick(note) }
            }

            override fun getOnLongClickListener(note: PreviewNote): OnLongClickListener {
                return OnLongClickListener {
                    noteListViewListener?.onNoteLongClick(note)
                    true
                }
            }
        }
    }

    private fun getDefaultNoteListViewListener() = object : NoteListViewListener {
        override fun onNoteClick(note: PreviewNote) = Unit
        override fun onNoteLongClick(note: PreviewNote) = Unit
        override fun onNoteListRefresh() = Unit
    }

    private fun getOnRefreshListener() = OnRefreshListener {
        noteListViewListener?.onNoteListRefresh()
        isRefreshing = false
    }


    /*--- Action Handling Interface ---*/

    interface NoteListViewListener {
        fun onNoteClick(note: PreviewNote)
        fun onNoteLongClick(note: PreviewNote)
        fun onNoteListRefresh()
    }

}
