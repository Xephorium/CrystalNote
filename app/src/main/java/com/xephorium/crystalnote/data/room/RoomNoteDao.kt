package com.xephorium.crystalnote.data.room

import androidx.room.*
import com.xephorium.crystalnote.data.model.Note

@Dao
interface RoomNoteDao {

    @Insert
    fun insertNote(note: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Int): Note

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT * from notes")
    fun getNotes() : List<Note>
}