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
    var color: Int,
    var password: String = ""
) {
    companion object {
        const val NO_NOTE: Int = 0
        const val NO_COLOR: Int = 17
    }
}
