package com.xephorium.crystalnote.data.repository

import android.os.Environment

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

/*
  NoteDiskRepository                                    05.11.2019
  Christopher Cruzen

    Manages read/write to note files in Android's external storage.
*/

class NoteDiskRepository() {


    /*--- Public Read/Write Methods ---*/

    fun exportNoteToDownloads(name: String, content: String) {
        val noteFile = getNoteFile(name)

        initializeFile(noteFile)

        writeToFile(noteFile, content)
    }


    /*--- Private Methods ---*/

    private fun getNoteFile(name: String): File {
        val fileName = getNoteName(name)
        return File(getDownloadsDirectory().toString() + "/" + fileName)
    }

    private fun getNoteName(name: String): String {
        val downloadFiles = getDownloadsDirectory().listFiles().map { it.name }
        var fileName = name + FILE_EXTENSION
        var index = 1

        while (downloadFiles.contains(fileName)) {
            index++
            fileName = "$name($index)$FILE_EXTENSION"
        }

        return fileName
    }

    private fun getDownloadsDirectory(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }

    private fun initializeFile(file: File) {
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


    /*--- Constants ---*/

    companion object {
        private const val FILE_EXTENSION = ".txt"
    }
}
