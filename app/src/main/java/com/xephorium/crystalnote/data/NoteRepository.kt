package com.xephorium.crystalnote.data

import android.content.Context
import android.os.Environment

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.utility.NoteUtility

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.ArrayList
import java.util.Date

/*
  NoteRepository                                        05.11.2019
  Christopher Cruzen

    Manages read/write to note files in Android's external storage.
*/

class NoteRepository(private val context: Context) {


    /*--- Object Initialization ---*/

    init {
        createNotesDirectory()
    }


    /*--- Public Read/Write Methods ---*/

    fun getNotes(): List<Note> {
        val notesList = getNotesDirectory().listFiles()

        //if (notesList.isEmpty()) {
        //    writeToNote(DEFAULT_NOTE_NAME, DEFAULT_NOTE_TEXT)
        //    notesList = getNotesDirectory().listFiles()
        //}

        val notes = mutableListOf<Note>()
        notesList.forEach { file ->
            notes.add(Note(
                    color = NoteUtility.getDefaultColor(),
                    name = getNoteName(file),
                    date = getNoteDate(file),
                    path = getNotePath(file),
                    preview = getNotePreview(file)
            ))
        }

        return notes
    }

    fun getNote(name: String): Note? {
        return getNotes().firstOrNull { currentNote -> currentNote.name == name }
    }

    fun writeToNote(name: String, content: String): Boolean {
        val note = getNoteFile(name)

        if (!note.exists())
            createNote(name)

        try {
            val noteOutputStream = FileOutputStream(note)
            val printWriter = PrintWriter(noteOutputStream)
            printWriter.print(content)
            printWriter.flush()
            printWriter.close()
            noteOutputStream.close()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun readNoteContents(name: String): String {
        val note = getNoteFile(name)
        if (!note.exists()) return ""

        try {
            val inputStream = FileInputStream(note.toString())
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()

            var nextLine = bufferedReader.readLine()
            while (nextLine != null) {
                stringBuilder.append(nextLine)
                stringBuilder.append("\n")
                nextLine = bufferedReader.readLine()
            }
            inputStream.close()

            return stringBuilder.toString()

        } catch (e: Exception) {
            return ""
        }
    }

    fun deleteNote(name: String) {
        if (noteExists(name)) {
            try {
                getNoteFile(name).delete()
            } catch (e: Exception) {
                // Do Nothing
            }
        }
    }

    fun noteExists(name: String): Boolean {
        getNotes().forEach { note ->
            if (note.name == name) return true
        }
        return false
    }


    /*--- Private File Management Methods ---*/

    private fun createNote(name: String) {
        if (!noteExists(name)) {
            try {
                getNoteFile(name).createNewFile()
            } catch (e: Exception) {
                // Do Nothing
            }
        }
    }

    private fun getNoteFile(note: Note): File {
        return File(getNotesDirectory().toString() + "/" + note.name + FILE_EXTENSION)
    }

    private fun getNoteFile(name: String): File {
        return File(getNotesDirectory().toString() + "/" + name + FILE_EXTENSION)
    }

    private fun getNoteName(note: File): String {
        return note.toString().substring(getNotesDirectory().toString().length
                + "/".length, note.toString().length - FILE_EXTENSION.length)
    }

    private fun getNoteDate(note: File): Date {
        return Date(note.lastModified())
    }

    private fun getNotePath(note: File): String {
        return note.path
    }

    private fun getNotePreview(note: File): String {
        return NoteUtility.getPreview(readNoteContents(getNoteName(note)))
    }


    /*--- Private Directory Management Methods ---*/

    /* Note: Method returns a File object pointing to the external device directory
     *       <app home>/notes. When passed the parameter DIRECTORY_DCIM, getExternalFilesDir()
     *       returns <app home>/files/DCIM. This method then extracts <app home>, deletes
     *       the newly created DCIM and files folders, and returns the final notes directory.
     */
    private fun getNotesDirectory(): File {
        return context.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.let {

            // Delete DCIM Directory
            it.delete()

            // Delete files Directory
            val filesDirectory =
                    it.toString().substring(0, it.toString().length - "/DCIM".length)
            File(filesDirectory).delete()

            // Assemble & Return Notes Directory
            val notesDirectory =
                    it.toString().substring(0, it.toString().length - "/files/DCIM".length) +
                            "/" + DIRECTORY_NAME
            File(notesDirectory)
        }
    }

    private fun createNotesDirectory() {
        val directory = getNotesDirectory()
        if (!directory.exists()) directory.mkdir()
    }

    private fun isNotesDirectoryEmpty(): Boolean {
        return getNotesDirectory().listFiles().isEmpty()
    }

    private fun populateNotesDirectory() {
        writeToNote(DEFAULT_NOTE_NAME, DEFAULT_NOTE_TEXT)

        // Test Data
        writeToNote("Shopping List", "- Bread\n- Milk\n- Eggs\n- Sugar")
        writeToNote("Beautiful Vistas", "- Shipyard, Halo Reach\n- All of Skyrim\n- Winterfell, The Forest")
        writeToNote("Reasons To Code", "- Puzzle Solving!\n- Free To Learn\n- Hones Critical Thinking\n- It Pays Well")
        writeToNote("Cities to Visit", "- Chicago\n- New York\n- Seattle\n- San Francisco")
        writeToNote("A Final Note", "Building this app has been challenging. There were more than a few moments when I was ready to throw in the towel and accept that I'd learned enough. But each successfully rendered list item, formatted to responsive perfection has been a reminder of why I started: to create something beautiful. The world deserves a clean notes app for Android. It's about time someone put in the hours to code one.")
    }


    /*--- Constants ---*/

    companion object {
        private const val DIRECTORY_NAME = "notes"
        private const val FILE_EXTENSION = ".txt"
        private const val DEFAULT_NOTE_NAME = "Note"
        private const val DEFAULT_NOTE_TEXT = "• List Item #5\n• List Item #6\n\n• List Item #7\n• List Item #8\n\nLorem ipsum dolor sit amet, consecte adipiscing elit, sed do eiusmod tempor."
    }
}
