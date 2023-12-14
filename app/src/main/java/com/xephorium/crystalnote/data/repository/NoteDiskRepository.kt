package com.xephorium.crystalnote.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
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
        context.contentResolver.openInputStream(uri).let { inputStream ->

            // Read Name
            // TODO - Crashes on newer versions of Android API. Runs, but returns
            //        a junk path (and therefore name) on older versions. Will need
            //        a new method of retrieving Filename. ContentProvider?
            val name = "Fake Name" // uri.path?.let { path -> extractFileNameFromPath(path) } ?: ""

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

    fun getSanitizedExportFileName(name: String): String {
        return name.map { char ->
            if (RESERVED_CHARACTERS.contains(char)) REPLACEMENT_CHARACTER else char
        }.joinToString(separator = "", prefix = "").plus(FILE_EXTENSION)
    }

    fun writeStringToTextFile(uri: Uri, contents: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri).let { outputStream ->
                val writer = BufferedWriter(OutputStreamWriter(outputStream))
                writer.write(contents)
                writer.close()
            }
            true
        } catch (exception: Exception) {
            false
        }
    }

    @Deprecated("Direct file access has been disabled in Android 12.")
    fun exportNoteToDownloads(name: String, content: String) {
        val noteFile = getNoteFile(name)

        initializeFile(noteFile)

        writeToFile(noteFile, content)
    }


    /*--- Private Methods ---*/

    private fun extractFileNameFromPath(path: String): String {
        val fileNameWithExtension =
            path.substring(path.indexOfLast { char -> char == '/' } + 1, path.length)
        return if (fileNameWithExtension.contains(FILE_EXTENSION))
            fileNameWithExtension.substring(0, fileNameWithExtension.indexOf(FILE_EXTENSION))
        else
            fileNameWithExtension
    }

    @Deprecated("Direct file access has been disabled in Android 12.")
    private fun getNoteFile(name: String): File {
        val fileName = getNoteName(name)
        return File(getDownloadsDirectory().toString() + "/" + fileName)
    }

    @Deprecated("Direct file access has been disabled in Android 12.")
    private fun getNoteName(name: String): String {
        var fileName = getSanitizedExportFileName(name) + FILE_EXTENSION
        var file = File(getDownloadsDirectory().toString() + "/" + fileName)
        var index = 1

        while (file.exists()) {
            index++
            fileName = "$name($index)$FILE_EXTENSION"
            file = File(getDownloadsDirectory().toString() + "/" + fileName)
            System.out.println("LLAMA - Trying new name: " + fileName)
        }

        System.out.println("LLAMA - Name Selected: " + fileName)
        return fileName
    }

    @Deprecated("Direct file access has been disabled in Android 12.")
    private fun initializeFile(file: File) {
        // TODO - File writing has changed in apps targeting newer versions of Android.
        //        This throws an exception and needs to be revisited.
        if (!file.exists()) try {
            file.createNewFile()
        } catch (e: Exception) {
            println("LLAMA - File writing failed: " + e.toString())
        }
    }

    @Deprecated("Direct file access has been disabled in Android 12.")
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
