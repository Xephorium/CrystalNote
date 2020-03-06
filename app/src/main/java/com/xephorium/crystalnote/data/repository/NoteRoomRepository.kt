package com.xephorium.crystalnote.data.repository

import android.content.Context

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.room.RoomRepository
import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.ui.custom.ThemePreview

import java.util.*

/*
  NoteRoomRepository                                    02.26.2020
  Christopher Cruzen

    Manages read/write to notes stored in a Room database.
*/

class NoteRoomRepository(context: Context) {


    /*--- Variable Declarations ---*/

    private var roomRepository: RoomRepository = RoomRepository(context)


    /*--- Public Read/Write Methods ---*/

    fun insertNote(name: String, contents: String, color: Int) {
        roomRepository.insertNote(
            Note(
                name = name,
                contents = contents,
                date = Calendar.getInstance().time,
                color = color
            )
        )
    }

    fun getNote(id: Int): Note? {
        return roomRepository.getNote(id)
    }

    fun updateNote(id: Int, name: String, contents: String, color: Int) {
        roomRepository.updateNote(
            Note(
                id = id,
                name = name,
                contents = contents,
                date = Calendar.getInstance().time,
                color = color
            )
        )
    }

    fun deleteNote(id: Int) {
        if (noteExists(id)) {
            roomRepository.getNotes().firstOrNull { id == it.id }?.let {
                roomRepository.deleteNote(it)
            }
        }
    }

    fun getNotes(): List<Note> {
        return roomRepository.getNotes()
    }

    fun noteExists(id: Int): Boolean {
        return roomRepository.getNotes().any { note -> id == note.id }
    }

    // Note: To be used only by NotesWidgetProvider, which creates
    //       a separate thread for the database query internally.
    fun getNoteSynchronously(id: Int): Note? {
        return roomRepository.getNoteSynchronously(id)
    }


    /*--- Developer Utilities ---*/

    fun populateNotesDatabase() {
        DEVELOPER_NOTES.forEach { note ->
            roomRepository.insertNote(note)
        }
    }

    companion object {
        private val DATE_OF_FIRST_COMMIT: Long = 1493355600000
        private val ONE_MINUTE: Long = 60000
        private val DEVELOPER_NOTES = listOf(
            Note(
                id = Note.NO_NOTE,
                name = "Note",
                contents = "• List Item #5\n• List Item #6\n\n• List Item #7\n• List Item #8\n\nLorem ipsum dolor sit amet, consecte adipiscing elit, sed do eiusmod tempor.",
                date = Date(DATE_OF_FIRST_COMMIT + (ONE_MINUTE * 5)),
                color = ThemePreview.NOTE_COLORS[0]
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Beautiful Vistas",
                contents = "- Shipyard, Halo Reach\n- All of Skyrim\n- Winterfell, The Forest",
                date = Date(DATE_OF_FIRST_COMMIT + (ONE_MINUTE * 4)),
                color = ThemePreview.NOTE_COLORS[1]
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Shopping List",
                contents = "- Bread\n- Milk\n- Eggs\n- Sugar",
                date = Date(DATE_OF_FIRST_COMMIT + (ONE_MINUTE * 3)),
                color = ThemePreview.NOTE_COLORS[2]
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Reasons To Code",
                contents = "- Puzzle Solving!\n- Free To Learn\n- Hones Critical Thinking\n- It Pays Well",
                date = Date(DATE_OF_FIRST_COMMIT + (ONE_MINUTE * 2)),
                color = ThemePreview.NOTE_COLORS[3]
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Cities to Visit",
                contents = "- Chicago\n- New York\n- Seattle\n- San Francisco",
                date = Date(DATE_OF_FIRST_COMMIT + ONE_MINUTE),
                color = ThemePreview.NOTE_COLORS[4]
            ),
            Note(
                id = Note.NO_NOTE,
                name = "A Final Note",
                contents = "Building this app has been challenging. There were more than a few moments when I was ready to throw in the towel and accept that I'd learned enough. But each successfully rendered list item, formatted to responsive perfection has been a reminder of why I started: to create something beautiful. The world deserves a clean notes app for Android. It's about time someone put in the hours to code one.",
                date = Date(DATE_OF_FIRST_COMMIT),
                color = ThemePreview.NOTE_COLORS[6]
            )
        )
    }
}
