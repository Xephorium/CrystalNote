package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.data.model.Note
import kotlinx.android.synthetic.main.note_list_header.view.*
import kotlinx.android.synthetic.main.note_list_item.view.*

/*
  NoteListAdapter                                                        05.12.2019
  Christopher Cruzen

    This class provides display logic and handling behavior for each element of the
  NoteListView.

*/

open class NoteListAdapter(
        private val context: Context,
        private val newNotes: List<Note>,
        private val oldNotes: List<Note>
) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {


    /*--- Private Variables ---*/

    private val sharedPreferencesRepository = SharedPreferencesRepository(context)
    private val shouldShowColorBar = sharedPreferencesRepository.getNoteColorsEnabled()
    private val shouldShowTodayHeader = sharedPreferencesRepository.getTodayHeaderEnabled()


    /*--- Lifecycle Methods ---*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            VIEW_TYPE_NOTE -> {
                val view = inflater.inflate(R.layout.note_list_item, parent, false)
                ViewHolder(view, VIEW_TYPE_NOTE)
            }
            else -> {
                val view = inflater.inflate(R.layout.note_list_header, parent, false)
                ViewHolder(view, VIEW_TYPE_HEADER)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder.type == VIEW_TYPE_HEADER) {
            if (position == 0) {
                holder.header.text = context.resources.getString(R.string.noteListToday)
            } else {
                holder.header.text = context.resources.getString(R.string.noteListOlder)
                holder.space.visibility = View.VISIBLE
            }
        }

        if (holder.type == VIEW_TYPE_NOTE) {
            val note = getNoteFromPosition(position)
            holder.name.text = note.name
            holder.preview.text = note.preview
            holder.date.text = NoteUtility.getFormattedDate(note)
            DrawableCompat.setTint(holder.colorBar.background, note.color)
            if (!shouldShowColorBar) holder.colorBar.visibility = View.GONE
            holder.setNoteClickListeners(note)
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int {
        return when {
            newNotes.isEmpty() && oldNotes.isEmpty() -> 0
            newNotes.isNotEmpty() && oldNotes.isEmpty() -> newNotes.size
            newNotes.isEmpty() && oldNotes.isNotEmpty() -> oldNotes.size
            else -> newNotes.size + oldNotes.size + 2
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            newNotes.isNotEmpty() && oldNotes.isEmpty() -> VIEW_TYPE_NOTE
            newNotes.isEmpty() && oldNotes.isNotEmpty() -> VIEW_TYPE_NOTE
            else -> {
                when {
                    position == 0 -> VIEW_TYPE_HEADER
                    position < (newNotes.size + 1) -> VIEW_TYPE_NOTE
                    position == (newNotes.size + 1) -> VIEW_TYPE_HEADER
                    else -> VIEW_TYPE_NOTE
                }
            }
        }
    }

    private fun getNoteFromPosition(position: Int): Note {
        return when {
            newNotes.isNotEmpty() && oldNotes.isEmpty() -> newNotes[position]
            newNotes.isEmpty() && oldNotes.isNotEmpty() -> oldNotes[position]
            else -> {
                if (position <= newNotes.size) newNotes[position - 1]
                else oldNotes[position - newNotes.size - 2]
            }
        }
    }

    inner class ViewHolder internal constructor(val view: View, val type: Int) :
            RecyclerView.ViewHolder(view) {

        internal lateinit var name: TextView
        internal lateinit var preview: TextView
        internal lateinit var date: TextView
        internal lateinit var colorBar: View
        internal lateinit var header: TextView
        internal lateinit var space: Space

        init {
            if (type == VIEW_TYPE_NOTE) {
                name = view.textNoteListTitle
                preview = view.textNoteListPreview
                date = view.textNoteListDate
                colorBar = view.colorBarNoteList
            } else {
                header = view.textNoteListHeader
                space = view.space
            }
        }

        fun setNoteClickListeners(note: Note) {
            view.setOnClickListener(getOnClickListener(note))
            view.setOnLongClickListener(getOnLongClickListener(note))
        }
    }

    open fun getOnClickListener(note: Note): View.OnClickListener {
        return View.OnClickListener {
            // Default Behavior; Do Nothing
        }
    }

    open fun getOnLongClickListener(note: Note): View.OnLongClickListener {
        return View.OnLongClickListener {
            // Default Behavior; Do Nothing
            true
        }
    }


    /*--- Constants ---*/

    companion object {
        private const val VIEW_TYPE_NOTE = 177
        private const val VIEW_TYPE_HEADER = 264
    }
}