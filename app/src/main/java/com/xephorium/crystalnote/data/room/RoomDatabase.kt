package com.xephorium.crystalnote.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xephorium.crystalnote.data.model.Note

@Database(
    version = 2,
    entities = [Note::class],
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
@TypeConverters(RoomConverters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun noteRepository(): RoomNoteDao

    companion object {
        const val ROOM_DATABASE_NAME = "CRYSTAL_NOTE_DATABASE"
    }
}