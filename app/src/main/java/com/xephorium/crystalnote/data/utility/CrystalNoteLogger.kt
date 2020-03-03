package com.xephorium.crystalnote.data.utility

import android.content.Context
import android.os.Environment
import java.io.File
import java.lang.Exception
import java.io.BufferedWriter
import java.io.FileWriter

/*
   CrystalNoteLogger                                      03.02.2020
   Christopher Cruzen

     CrystalNoteLogger is a simple utility object that logs provided
   strings to a text file on the user's device. It is not intended to
   be used beyond development and should be switched off before final
   launch.

 */

object CrystalNoteLogger {


    /*--- Constants ---*/

    private const val LOGGING_ENABLED = true

    private const val DIRECTORY_NAME = "log"
    private const val LOG_NAME = "CrystalNoteLog.txt"


    /*--- Public Method ---*/

    fun log(context: Context, string: String) {
        if (LOGGING_ENABLED) {
            setupLogFile(context)

            try {
                val writer = BufferedWriter(FileWriter(getLogFile(context), true))
                writer.append(string)
                writer.append("\n")
                writer.close()
            } catch (exception: Exception) {
                // Fail Silently
            }
        }
    }


    /*--- Private Methods ---*/

    private fun setupLogFile(context: Context) {
        if (!getLogDirectory(context).exists())
            createLogDirectory(context)

        if (!getLogFile(context).exists())
            createLogFile(context)
    }

    private fun createLogFile(context: Context) {
        getLogFile(context).createNewFile()
    }

    private fun getLogFile(context: Context): File {
        return File(getLogDirectory(context).toString() + "/" + LOG_NAME)
    }

    private fun createLogDirectory(context: Context) {
        val directory = getLogDirectory(context)
        if (!directory.exists()) directory.mkdir()
    }

    /* Note: Method returns a File object pointing to the external device directory
     *       <app home>/log. When passed the parameter DIRECTORY_DCIM, getExternalFilesDir()
     *       returns <app home>/files/DCIM. This method then extracts <app home>, deletes
     *       the newly created DCIM and files folders, and returns the final log directory.
     */
    private fun getLogDirectory(context: Context): File {
        return context.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.let {

            // Delete DCIM Directory
            it.delete()

            // Delete files Directory
            val filesDirectory =
                it.toString().substring(0, it.toString().length - "/DCIM".length)
            File(filesDirectory).delete()

            // Assemble & Return Log Directory
            val notesDirectory =
                it.toString().substring(0, it.toString().length - "/files/DCIM".length) +
                        "/" + DIRECTORY_NAME
            File(notesDirectory)
        }
    }
}