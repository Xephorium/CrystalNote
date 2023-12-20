package com.xephorium.crystalnote.data.room

import androidx.room.*
import com.xephorium.crystalnote.data.model.Note

@Dao
interface RoomNoteDao {

    @Insert
    fun insertNote(note: Note): Long

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getFullNote(id: Int): Note

    @Update
    fun updateFullNote(note: Note)

    @Query("UPDATE notes SET password=:password WHERE id = :id")
    fun updateNotePassword(id: Int, password: String)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT id as id, name as name, preview as preview, date as date, color as color, password as password from notes")
    fun getLightweightNotes() : List<Note>
}