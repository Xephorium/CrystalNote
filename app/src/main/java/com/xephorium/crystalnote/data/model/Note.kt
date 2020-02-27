package com.xephorium.crystalnote.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var contents: String,
    var date: Date,
    var color: Int
) {
    companion object {
        const val DEFAULT_NOTE_ID: Int = 0
    }
}
