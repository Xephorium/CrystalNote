package com.xephorium.crystalnote.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xephorium.crystalnote.data.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun noteRepository(): RoomNoteDao

    companion object {
        const val ROOM_DATABASE_NAME = "CRYSTAL_NOTE_DATABASE"
    }
}