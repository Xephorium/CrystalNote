package com.xephorium.crystalnote.data.utility

import android.graphics.Color

import com.xephorium.crystalnote.data.model.Note

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import java.util.Random

object NoteUtility {


    /*--- Constants ---*/

    private const val PREVIEW_LENGTH = 50

    enum class SortType {
        DATE_OLD,
        DATE_NEW,
        NAME_APLHA,
        NAME_REVERSE_APLHA
    }


    /*--- Public Utility Methods ---*/

    fun getDefaultColor(): Int = Color.parseColor("#aaaaaa")

    fun getRandomColor(): Int {
        val colors = intArrayOf(
                Color.parseColor("#e91e63"),
                Color.parseColor("#f44336"),
                Color.parseColor("#673ab7"),
                Color.parseColor("#3f51b5"),
                Color.parseColor("#2196f3"),
                Color.parseColor("#4caf50"),
                Color.parseColor("#009688"),
                Color.parseColor("#ff5722"),
                Color.parseColor("#795548"),
                Color.parseColor("#9e9e9e"),
                Color.parseColor("#607d8b")
        )
        return colors[Random().nextInt(colors.size - 1)]
    }

    fun getNoteFromList(noteList: List<Note>?, noteName: String): Note? {
        return noteList?.firstOrNull { note -> note.name == noteName }
    }

    fun sortNotes(inputList: MutableList<Note>, type: SortType): List<Note> {
        val outputList: List<Note>

        when (type) {
            NoteUtility.SortType.DATE_NEW -> outputList = sortNotesByDateNew(inputList)

            NoteUtility.SortType.DATE_OLD -> outputList = sortNotesByDateOld(inputList)

            else -> outputList = ArrayList()
        }
        return outputList
    }

    fun getFormattedDate(note: Note): String {
        val currentDate = Calendar.getInstance().time
        val dayFormat = SimpleDateFormat("M.d.yy", Locale.US)
        val hourFormat = SimpleDateFormat("h:mma", Locale.US)

        return if (currentDate.day == note.date.day) {
            hourFormat.format(note.date).toLowerCase()
        } else {
            dayFormat.format(note.date)
        }
    }

    fun getPreview(c: String): String {
        var contents = c
        contents = contents.replace("\\n".toRegex(), " ")

        return if (contents.length >= PREVIEW_LENGTH) {
            contents.substring(0, PREVIEW_LENGTH)
        } else {
            contents
        }
    }


    /*--- Private Methods ---*/

    private fun sortNotesByDateNew(inputList: MutableList<Note>): List<Note> {
        for (x in inputList.indices) {
            for (y in inputList.size - 1 downTo x + 1) {
                if (inputList[y].date.after(inputList[y - 1].date)) {
                    val temp = inputList[y]
                    inputList[y] = inputList[y - 1]
                    inputList[y - 1] = temp
                }
            }
        }
        return inputList
    }

    private fun sortNotesByDateOld(inputList: MutableList<Note>): List<Note> {
        for (x in inputList.indices) {
            for (y in inputList.size - 1 downTo x + 1) {
                if (inputList[y].date.before(inputList[y - 1].date)) {
                    val temp = inputList[y]
                    inputList[y] = inputList[y - 1]
                    inputList[y - 1] = temp
                }
            }
        }
        return inputList
    }
}