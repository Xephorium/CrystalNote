package com.xephorium.crystalnote.data.repository

import android.content.Context

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.data.room.RoomRepository
import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.ui.custom.HomePreviewView

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

    fun insertNote(
        name: String,
        contents: String,
        color: Int,
        password: String,
        archived: Boolean
    ): Int {
        return roomRepository.insertNote(
            Note(
                name = name,
                contents = contents,
                preview = NoteUtility.getPreviewFromContents(contents),
                date = Calendar.getInstance().time,
                color = color,
                password = password,
                archived = archived
            )
        ).toInt()
    }

    fun getNote(id: Int): Note? {
        return roomRepository.getNote(id)
    }

    fun updateNote(
        id: Int,
        name: String,
        contents: String,
        color: Int,
        password: String,
        archived: Boolean
    ) {
        roomRepository.updateNote(
            Note(
                id = id,
                name = name,
                contents = contents,
                preview = NoteUtility.getPreviewFromContents(contents),
                date = Calendar.getInstance().time,
                color = color,
                password = password,
                archived = archived
            )
        )
    }

    fun updateNotePassword(id: Int, password: String) {
        roomRepository.updateNotePassword(id, password)
    }

    fun updateNoteArchival(id: Int, isArchived: Boolean) {
        roomRepository.updateNoteArchival(id, isArchived)
    }

    /* Creates preview field for note and updates entry in database when no
     * other value has changed. Importantly, this method preserves the existing
     * entry's last edit time and date.
     */
    fun migrateNote(
        id: Int,
        name: String,
        contents: String,
        date: Date,
        color: Int,
        password: String,
        archived: Boolean
    ) {
        roomRepository.updateNote(
            Note(
                id = id,
                name = name,
                contents = contents,
                preview = NoteUtility.getPreviewFromContents(contents),
                date = date,
                color = color,
                password = password,
                archived = archived
            )
        )
    }

    fun deleteNote(id: Int) {
        if (noteExists(id)) {
            roomRepository.deleteNote(id)
        }
    }

    /* Reads all notes into lightweight PreviewNote objects, which contain
     * all note information except the full content and are used to efficiently
     * populate most screens in the app.
     */
    fun getPreviewNotes(): List<PreviewNote> {
        return roomRepository.getPreviewNotes()
    }

    fun noteExists(id: Int): Boolean {
        return roomRepository.getPreviewNotes().any { note -> id == note.id }
    }

    // Note: To be used only by NotesWidgetProvider, which creates
    //       a separate thread for the database query internally.
    fun getNoteSynchronously(id: Int): Note {
        return roomRepository.getNoteSynchronously(id)
    }


    /*--- Developer Utilities ---*/

    fun populateNotesDatabase() {
        DEVELOPER_NOTES.forEach { note ->
            roomRepository.insertNote(note)
        }
    }

    companion object {
        private const val DATE_OF_FIRST_COMMIT: Long = 1493355600000
        private const val ONE_MINUTE: Long = 60000
        private val DEVELOPER_NOTES = listOf(
            Note(
                id = Note.NO_NOTE,
                name = "Note",
                contents = "• List Item #5\n• List Item #6\n\n• List Item #7\n• List Item #8\n\nLorem ipsum dolor sit amet, consecte adipiscing elit, sed do eiusmod tempor.",
                preview = "",
                date = Date(DATE_OF_FIRST_COMMIT + (ONE_MINUTE * 5)),
                color = HomePreviewView.NOTE_COLORS[0],
                archived = false
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Beautiful Vistas",
                contents = "• Shipyard, Halo Reach\n• All of Skyrim\n• Winterfell, The Forest",
                preview = "",
                date = Date(DATE_OF_FIRST_COMMIT + (ONE_MINUTE * 4)),
                color = HomePreviewView.NOTE_COLORS[1],
                archived = false
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Shopping List",
                contents = "• Bread\n• Milk\n• Eggs\n• Sugar",
                preview = "",
                date = Date(DATE_OF_FIRST_COMMIT + (ONE_MINUTE * 3)),
                color = HomePreviewView.NOTE_COLORS[2],
                archived = false
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Reasons To Code",
                contents = "• Puzzle Solving!\n• Free To Learn\n• Hones Critical Thinking\n• It Pays Well",
                preview = "",
                date = Date(DATE_OF_FIRST_COMMIT + (ONE_MINUTE * 2)),
                color = HomePreviewView.NOTE_COLORS[3],
                archived = false
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Cities to Visit",
                contents = "• Chicago\n• New York\n• Seattle\n• San Francisco",
                preview = "",
                date = Date(DATE_OF_FIRST_COMMIT + ONE_MINUTE),
                color = HomePreviewView.NOTE_COLORS[4],
                archived = false
            ),
            Note(
                id = Note.NO_NOTE,
                name = "Lorem Ipsum",
                contents = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                preview = "",
                date = Date(DATE_OF_FIRST_COMMIT),
                color = HomePreviewView.NOTE_COLORS[6],
                archived = false
            ),
//            Note(
//                id = Note.NO_NOTE,
//                name = "A Final Note",
//                contents = "Building this app has been challenging. There were more than a few moments when I was ready to throw in the towel and accept that I'd learned enough. But each successfully rendered list item, formatted to responsive perfection has been a reminder of why I started: to create something beautiful. The world deserves a clean notes app for Android. It's about time someone put in the hours to code one.",
//                preview = "",
//                date = Date(DATE_OF_FIRST_COMMIT),
//                color = HomePreviewView.NOTE_COLORS[6],
//                archived = false
//            )
        )
    }
}
