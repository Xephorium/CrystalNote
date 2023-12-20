package com.xephorium.crystalnote.data.room

import android.content.Context
import androidx.room.Room
import com.xephorium.crystalnote.data.model.Note
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class RoomRepository(context: Context) {


    /*--- Variable Declarations ---*/

    private val roomDatabase = Room.databaseBuilder(
        context,
        RoomDatabase::class.java,
        RoomDatabase.ROOM_DATABASE_NAME
    ).build()


    /*--- Database Access Methods ---*/

    fun insertNote(note: Note): Long {
        val callable = Callable {
            roomDatabase.noteRepository().insertNote(note)
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        return future.get()
    }

    fun getFullNote(id: Int) : Note {
        val callable = Callable {
            roomDatabase.noteRepository().getFullNote(id)
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        return future.get()
    }

    fun updateFullNote(note: Note) {
        Executors.newSingleThreadExecutor().submit(Callable {
            roomDatabase.noteRepository().updateFullNote(note)
        })
    }

    fun updateNotePassword(id: Int, password: String) {
        Executors.newSingleThreadExecutor().submit(Callable {
            roomDatabase.noteRepository().updateNotePassword(id, password)
        })
    }

    fun deleteNote(note: Note) {
        Executors.newSingleThreadExecutor().submit(Callable {
            roomDatabase.noteRepository().deleteNote(note)
        })
    }

    fun getLightweightNotes() : List<Note> {
        val callable = Callable<List<Note>> {
            roomDatabase.noteRepository().getLightweightNotes()
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        return future.get()
    }

    fun getFullNoteSynchronously(id: Int) : Note {
        return roomDatabase.noteRepository().getFullNote(id)
    }

}