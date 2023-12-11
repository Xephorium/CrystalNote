package com.xephorium.crystalnote.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.model.Note.Companion.NO_NOTE
import com.xephorium.crystalnote.data.utility.CrystalNoteToast
import com.xephorium.crystalnote.data.utility.NoteUtility
import java.io.*

import java.util.*

/*
  NoteDiskRepository                                          03.13.2020
  Christopher Cruzen

    Manages read/write to plaintext files in Android's external storage.
*/

class NoteDiskRepository(private val context: Context) {


    /*--- Public Read/Write Methods ---*/

    fun exportNoteToDownloads(name: String, content: String) {
        val noteFile = getNoteFile(name)

        initializeFile(noteFile)

        writeToFile(noteFile, content)
    }

    fun readPlaintextFile(uri: Uri): Note {
        context.contentResolver.openInputStream(uri).let { inputStream ->

            // Read Name
            val name = uri.path?.let { path -> extractFileNameFromPath(path) } ?: ""

            // Read Contents
            val reader = BufferedReader(InputStreamReader(inputStream))
            val contents = StringBuilder()
            var line = reader.readLine()
            while (line != null) {
                contents.append(line).append('\n')
                line = reader.readLine()
            }

            return Note(
                    id = NO_NOTE,
                    name = name,
                    contents = contents.toString(),
                    date = Date(),
                    color = NoteUtility.getDefaultColor()
            )
        }
    }

    fun writePlaintextFile(uri: Uri, contents: String): Boolean {
        try {
            context.contentResolver.openOutputStream(uri).let { outputStream ->

                // Write Contents
                val writer = BufferedWriter(OutputStreamWriter(outputStream))
                writer.write(contents)
                writer.close()

                return true
            }
        } catch (exception: Exception) {
            return false
        }
    }


    /*--- Private Methods ---*/

    private fun getNoteFile(name: String): File {
        val fileName = getNoteName(name)
        return File(getDownloadsDirectory().toString() + "/" + fileName)
    }

    private fun getNoteName(name: String): String {
        // TODO - Permission needed to read from disk. This call returns null.
        val downloadFiles = getDownloadsDirectory().listFiles().map { it.name }
        var fileName = sanitizeNoteName(name) + FILE_EXTENSION
        var index = 1

        while (downloadFiles.contains(fileName)) {
            index++
            fileName = "$name($index)$FILE_EXTENSION"
        }

        return fileName
    }

    private fun sanitizeNoteName(name: String): String {
        return name.map { char ->
            if (RESERVED_CHARACTERS.contains(char)) REPLACEMENT_CHARACTER else char
        }.joinToString(separator = "", prefix = "")
    }

    private fun getDownloadsDirectory(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }

    private fun initializeFile(file: File) {
        // TODO - File writing has changed in apps targeting newer versions of Android.
        //        This throws an exception and needs to be revisited.
        if (!file.exists()) try {
            file.createNewFile()
        } catch (e: Exception) {
            // Do Nothing
        }
    }

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

    private fun extractFileNameFromPath(path: String): String {
        val fileNameWithExtension =
                path.substring(path.indexOfLast { char -> char == '/' } + 1, path.length)
        return if (fileNameWithExtension.contains(FILE_EXTENSION))
            fileNameWithExtension.substring(0, fileNameWithExtension.indexOf(FILE_EXTENSION))
        else
            fileNameWithExtension
    }


    /*--- Constants ---*/

    companion object {
        private const val FILE_EXTENSION = ".txt"
        private const val REPLACEMENT_CHARACTER = '_'
        private val RESERVED_CHARACTERS = listOf(
                '|',
                '?',
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
    }
}
