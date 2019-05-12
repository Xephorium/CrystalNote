package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.data.model.Note

/*
  NoteListAdapter                                                           05.12.2019
  Christopher Cruzen

    Provides view behavior for each element of the NoteList and a default onClickListener.
  Per implementation, getOnClickListener should be overridden for desired behavior.

*/

open class NoteListAdapter(noteList: List<Note>) : BaseAdapter() {

    var notes: List<Note>? = null
        private set

    init {
        notes = noteList
    }

    override fun getView(position: Int, noteLayout: View?, parent: ViewGroup): View {
        var noteLayout = noteLayout
        if (noteLayout == null) {
            val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            noteLayout = inflater.inflate(R.layout.note_layout, parent, false)

            val noteTitle = noteLayout!!.findViewById<View>(R.id.note_title) as TextView
            val notePreview = noteLayout.findViewById<View>(R.id.note_preview) as TextView
            val noteDate = noteLayout.findViewById<View>(R.id.note_date) as TextView
            val noteIcon = noteLayout.findViewById<View>(R.id.note_icon) as ImageView

            noteTitle.text = notes!![position].name
            notePreview.text = notes!![position].preview
            noteDate.text = NoteUtility.getFormattedDate(notes!![position])
            noteIcon.setColorFilter(notes!![position].color, PorterDuff.Mode.SRC_ATOP)

            noteLayout.setOnClickListener(getOnClickListener(position))
            noteLayout.setOnLongClickListener(getOnLongClickListener(position))
        }

        return noteLayout
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return notes!![position]
    }

    override fun getCount(): Int {
        return if (notes != null) notes!!.size else 0
    }

    open fun getOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            // Default Behavior; Do Nothing
        }
    }

    open fun getOnLongClickListener(position: Int): View.OnLongClickListener {
        return View.OnLongClickListener {
            // Default Behavior; Do Nothing
            true
        }
    }
}