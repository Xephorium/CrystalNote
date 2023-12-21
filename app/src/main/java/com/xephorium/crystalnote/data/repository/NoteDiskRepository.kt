package com.xephorium.crystalnote.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.model.Note.Companion.NO_NOTE
import com.xephorium.crystalnote.data.utility.NoteUtility
import java.io.*

import java.util.*

/*
  NoteDiskRepository                                          12.12.2023
  Christopher Cruzen

    Manages read/write to plaintext files in Android's external storage.
*/

class NoteDiskRepository(private val context: Context) {


    /*--- Public Read/Write Methods ---*/

    fun readNoteFromTextFile(uri: Uri): Note {
        val contents = getFileContentsFromUri(uri)
        return Note(
            id = NO_NOTE,
            name = getFileNameFromUri(uri),
            preview = NoteUtility.getPreviewFromContents(contents),
            contents = contents,
            date = Date(),
            color = NoteUtility.getDefaultColor()
        )
    }

    fun getSanitizedExportFileName(name: String): String {
        return name.map { char ->
            if (RESERVED_CHARACTERS.contains(char)) REPLACEMENT_CHARACTER else char
        }.joinToString(separator = "", prefix = "").plus(FILE_EXTENSION)
    }

    fun writeStringToTextFile(uri: Uri, contents: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri, "rwt").let { outputStream ->
                val writer = BufferedWriter(OutputStreamWriter(outputStream))
                writer.write(contents)
                writer.close()
            }
            true
        } catch (exception: Exception) {
            false
        }
    }

    @Deprecated("Direct file access has been disabled in Android 11.")
    fun exportNoteToDownloads(name: String, content: String): Boolean {
        val noteFile = getNoteFile(name)

        initializeFile(noteFile)

        return writeToFile(noteFile, content)
    }


    /*--- Private Methods ---*/

    private fun getFileNameFromUri(uri: Uri): String {
        var fileName = DEFAULT_FILE_NAME
        context.contentResolver.query(uri, null, null, null, null)?.let {
            it.moveToFirst()
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0) fileName = it.getString(index)
            it.close()
        }
        return fileName
    }

    private fun getFileContentsFromUri(uri: Uri): String {
        context.contentResolver.openInputStream(uri).let { inputStream ->
            val reader = BufferedReader(InputStreamReader(inputStream))
            val contents = StringBuilder()
            var line = reader.readLine()
            while (line != null) {
                contents.append(line).append('\n')
                line = reader.readLine()
            }
            return contents.toString()
        }
    }

    @Deprecated("Direct file access has been disabled in Android 11.")
    private fun getNoteFile(name: String): File {
        val fileName = getNoteName(name)
        return File(getDownloadsDirectory().toString() + "/" + fileName)
    }

    @Deprecated("Direct file access has been disabled in Android 11.")
    private fun getNoteName(name: String): String {
        var fileName = getSanitizedExportFileName(name) + FILE_EXTENSION
        var file = File(getDownloadsDirectory().toString() + "/" + fileName)
        var index = 1

        while (file.exists()) {
            index++
            fileName = "$name($index)$FILE_EXTENSION"
            file = File(getDownloadsDirectory().toString() + "/" + fileName)
        }

        return fileName
    }

    @Deprecated("Direct file access has been disabled in Android 11.")
    private fun initializeFile(file: File) {
        if (!file.exists()) try {
            file.createNewFile()
        } catch (e: Exception) {
            // Do Nothing
        }
    }

    @Deprecated("Direct file access has been disabled in Android 11.")
    private fun writeToFile(file: File, content: String): Boolean {
        return try {
            val noteOutputStream = FileOutputStream(file)
            val printWriter = PrintWriter(noteOutputStream)
            printWriter.print(content)
            printWriter.flush()
            printWriter.close()
            noteOutputStream.close()
            true
        } catch (e: Exception) {
            false
        }
    }


    /*--- Constants ---*/

    companion object {
        private const val DEFAULT_FILE_NAME = "Opened File"
        private const val FILE_EXTENSION = ".txt"
        private const val REPLACEMENT_CHARACTER = ""
        private val RESERVED_CHARACTERS = listOf(
                '|',
                '?',
                '!',
                '*',
                '<',
                '\\',
                '"',
                ':',
                '>',
                '+',
                '[',
                ']',
                '/'
        )

        fun getDownloadsDirectory(): File {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        }
    }
}
