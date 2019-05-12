package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.data.model.Note
import kotlinx.android.synthetic.main.note_layout.view.*

/*
  NoteListAdapter                                                           05.12.2019
  Christopher Cruzen

    Provides view behavior for each element of the NoteList and a default onClickListener.
  Per implementation, getOnClickListener should be overridden for desired behavior.

*/

open class NoteListAdapter(private val notes: List<Note>) : BaseAdapter() {

    override fun getView(position: Int, noteLayout: View, parent: ViewGroup): View {

        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.note_layout, parent, false)

        layout.note_title.text = notes[position].name
        layout.note_preview.text = notes[position].preview
        layout.note_date.text = NoteUtility.getFormattedDate(notes[position])
        layout.note_icon.setColorFilter(notes[position].color, PorterDuff.Mode.SRC_ATOP)

        layout.setOnClickListener(getOnClickListener(position))
        layout.setOnLongClickListener(getOnLongClickListener(position))

        return layout
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return notes[position]
    }

    override fun getCount(): Int {
        return notes.size
    }

    private fun getOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            // Default Behavior; Do Nothing
        }
    }

    private fun getOnLongClickListener(position: Int): View.OnLongClickListener {
        return View.OnLongClickListener {
            // Default Behavior; Do Nothing
            true
        }
    }
}