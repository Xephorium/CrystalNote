package com.xephorium.crystalnote.data.utility

import android.graphics.Color
import android.text.format.DateUtils

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.model.PreviewNote

import java.text.SimpleDateFormat
import java.util.*

object NoteUtility {


    /*--- Constants ---*/

    private const val PREVIEW_LENGTH = 220

    enum class SortType {
        DATE_OLD,
        DATE_NEW,
        NAME_APLHA,
        NAME_REVERSE_APLHA
    }


    /*--- Public Utility Methods ---*/

    fun getDefaultColor(): Int = Color.parseColor("#7a7a7a")

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
                Color.parseColor("#607d8b")
        )
        return colors[Random().nextInt(colors.size - 1)]
    }

    fun getNoteFromList(noteList: List<Note>?, noteName: String): Note? {
        return noteList?.firstOrNull { note -> note.name == noteName }
    }

    fun sortPreviewNotes(inputList: MutableList<PreviewNote>, type: SortType): List<PreviewNote> {
        val outputList: List<PreviewNote> = when (type) {
            SortType.DATE_NEW -> sortPreviewNotesByDateNew(inputList)
            SortType.DATE_OLD -> sortPreviewNotesByDateOld(inputList)
            else -> ArrayList()
        }
        return outputList
    }

    fun getDynamicallyFormattedDate(note: PreviewNote): String {
        return if (DateUtils.isToday(note.date.time)) {
            getFormattedTime(note)
        } else {
            getFormattedDate(note)
        }
    }

    fun getFormattedDate(note: PreviewNote): String {
        return SimpleDateFormat("M/d/yy", Locale.US).format(note.date)
    }

    fun getFormattedTime(note: PreviewNote): String {
        return SimpleDateFormat("h:mma", Locale.US).format(note.date).lowercase(Locale.ROOT)
    }

    fun getFormattedDateTime(note: PreviewNote): String {
        val dayFormat = SimpleDateFormat("M/d/yy", Locale.US)
        val hourFormat = SimpleDateFormat("h:mma", Locale.US)
        return dayFormat.format(note.date) + " " + hourFormat.format(note.date).lowercase(Locale.ROOT)
    }

    fun getPreviewFromContents(contents: String): String {
        return if (contents.isNotEmpty()) {
            val trimmed = contents.replace("\\s+".toRegex(), " ").trim()
            if (trimmed.length > PREVIEW_LENGTH) trimmed.substring(0, PREVIEW_LENGTH)
            else trimmed.substring(0, trimmed.length)
        } else ""
    }


    /*--- Private Methods ---*/

    private fun sortPreviewNotesByDateNew(inputList: MutableList<PreviewNote>): List<PreviewNote> {
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

    private fun sortPreviewNotesByDateOld(inputList: MutableList<PreviewNote>): List<PreviewNote> {
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
