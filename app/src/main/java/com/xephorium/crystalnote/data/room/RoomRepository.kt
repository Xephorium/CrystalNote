package com.xephorium.crystalnote.data.room

import android.content.Context
import androidx.room.Room
import com.xephorium.crystalnote.data.model.Note
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class RoomRepository(context: Context) {


    /*--- Variable Declarations ---*/

    private val roomDatabase = Room.databaseBuilder(
        context, RoomDatabase::class.java,
        RoomDatabase.ROOM_DATABASE_NAME
    ).build()


    /*--- Database Access Methods ---*/

    fun insertNote(note: Note) {
        Executors.newSingleThreadExecutor().submit(Callable {
            roomDatabase.noteRepository().insertNote(note)
        })
    }

    fun getNote(id: Int) : Note {
        val callable = Callable<Note> {
            roomDatabase.noteRepository().getNote(id)
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        return future.get()
    }

    fun updateNote(note: Note) {
        Executors.newSingleThreadExecutor().submit(Callable {
            roomDatabase.noteRepository().updateNote(note)
        })
    }

    fun deleteNote(note: Note) {
        Executors.newSingleThreadExecutor().submit(Callable {
            roomDatabase.noteRepository().deleteNote(note)
        })
    }

    fun getNotes() : List<Note> {
        val callable = Callable<List<Note>> {
            roomDatabase.noteRepository().getNotes()
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        return future.get()
    }

    fun getNoteSynchronously(id: Int) : Note {
        return roomDatabase.noteRepository().getNote(id)
    }

}