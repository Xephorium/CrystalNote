package com.xephorium.crystalnote.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var contents: String,
    @ColumnInfo(defaultValue = "")
    var preview: String,
    var date: Date,
    var color: Int,
    var password: String = "",
    @ColumnInfo(defaultValue = "false")
    var archived: Boolean = false
) {
    companion object {
        const val NO_NOTE: Int = 0
    }
}
