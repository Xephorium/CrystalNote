package com.xephorium.crystalnote.data.room

import androidx.room.*
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.data.model.Note

@Dao
interface RoomNoteDao {

    @Insert
    fun insertNote(note: Note): Long

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Int): Note

    @Update
    fun updateNote(note: Note)

    @Query("UPDATE notes SET password=:password WHERE id = :id")
    fun updateNotePassword(id: Int, password: String)

    @Query("UPDATE notes SET archived=:archived WHERE id = :id")
    fun updateNoteArchival(id: Int, archived: Boolean)

    @Query("DELETE FROM notes WHERE id=:id")
    fun deleteNote(id: Int)

    @Query("SELECT id as id, name as name, preview as preview, date as date, color as color, password as password, archived as archived from notes")
    fun getPreviewNotes() : List<PreviewNote>
}