package com.xephorium.crystalnote.data.room

import android.content.Context
import androidx.room.Room
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.model.PreviewNote
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

    fun getNote(id: Int) : Note {
        val callable = Callable {
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

    fun updateNotePassword(id: Int, password: String) {
        Executors.newSingleThreadExecutor().submit(Callable {
            roomDatabase.noteRepository().updateNotePassword(id, password)
        })
    }

    fun deleteNote(id: Int) {
        Executors.newSingleThreadExecutor().submit(Callable {
            roomDatabase.noteRepository().deleteNote(id)
        })
    }

    fun getPreviewNotes() : List<PreviewNote> {
        val callable = Callable {
            roomDatabase.noteRepository().getPreviewNotes()
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        return future.get()
    }

    fun getNoteSynchronously(id: Int) : Note {
        return roomDatabase.noteRepository().getNote(id)
    }

}