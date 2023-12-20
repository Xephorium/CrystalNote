package com.xephorium.crystalnote.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var contents: String?,
    @ColumnInfo(defaultValue = "")
    var preview: String,
    var date: Date,
    var color: Int,
    var password: String = ""
) {
    companion object {
        const val NO_NOTE: Int = 0
        const val NOTE_ERROR_CONTENT: String = "Error reading note content."
        private const val PREVIEW_LENGTH = 220

        fun getPreviewFromContents(contents: String): String {
            return if (contents.isNotEmpty()) {
                val trimmed = contents.replace("\\s+".toRegex(), " ").trim()
                if (trimmed.length > PREVIEW_LENGTH) trimmed.substring(0, PREVIEW_LENGTH)
                else trimmed.substring(0, trimmed.length)
            } else ""
        }
    }
}
