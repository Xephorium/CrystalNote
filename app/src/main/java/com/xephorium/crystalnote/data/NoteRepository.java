package com.xephorium.crystalnote.data;

import android.content.Context;
import android.os.Environment;

import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.data.util.NoteUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
  NoteRepository.java                                   04.11.2017
  Christopher Cruzen

    Manages read/write to note files in Android's external storage.
*/

public class NoteRepository {


    /*--- Local Variables ---*/

    private final String DIRECTORY_NAME     = "notes";
    private final String FILE_EXTENSION     = ".txt";
    private final String DEFAULT_NOTE_NAME  = "Note";
    private final String DEFAULT_NOTE_TEXT  = "• List Item #5\n• List Item #6\n\n• List Item #7\n• List Item #8\n\nLorem ipsum dolor sit amet, consecte adipiscing elit, sed do eiusmod tempor.";
    private final Context context;


    /*--- Constructor ---*/

    public NoteRepository(Context context) {
        this.context = context;
        createNotesDirectory();
    }


    /*--- Read/Write Methods ---*/

    public List<Note> getNotes() {
        File[] notesList = getNotesDirectory().listFiles();

        if(notesList.length == 0) {
            writeToNote(DEFAULT_NOTE_NAME, DEFAULT_NOTE_TEXT);
            notesList = getNotesDirectory().listFiles();
        }

        List<Note> notes = new ArrayList<>();
        for (int x = 0; x < notesList.length; x++) {
            Note note = new Note();
            note.setName(getNoteName(notesList[x]));
            note.setDate(getNoteDate(notesList[x]));
            note.setPath(getNotePath(notesList[x]));
            note.setPreview(getNotePreview(notesList[x]));
            note.setColor(NoteUtils.getDefaultColor()); // NoteUtils.getRandomColor()
            notes.add(note);
        }

        return notes;
    }

    public Note getNote(String name) {
        List<Note> notes = getNotes();
        for (int x = 0; x < notes.size(); x++) {
            if (notes.get(x).getName().equals(name))
                return notes.get(x);
        }
        return null;
    }

    public String getNoteContents(File note)  {
        if(!note.exists()) {
            return "";
        }

        try {
            InputStream inputStream = new FileInputStream(note.toString());

            if(inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String nextLine = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((nextLine = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(nextLine);
                    stringBuilder.append("\n");
                }

                inputStream.close();
                return stringBuilder.toString();
            }
            return "";
        }
        catch (Exception e) {
            return "";
        }
    }

    public boolean writeToNote(String name, String content) {
        File note = getNoteFile(name);

        if(!note.exists())
            createNote(name);

        try {
            FileOutputStream noteOutputStream = new FileOutputStream(note);
            PrintWriter printWriter = new PrintWriter(noteOutputStream);
            printWriter.print(content);
            printWriter.flush();
            printWriter.close();
            noteOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createNote(String name) {
        File notesFile = new File(getNotesDirectory().toString() + "/" + name + FILE_EXTENSION);

        if(notesFile == null)
            return false;

        if(!notesFile.exists()) {
            try {
                return notesFile.createNewFile();
            } catch (Exception e) {
                return false;
            }
        } else {
            return true;
        }
    }

    public File getNoteFile(Note note) {
        return new File(getNotesDirectory().toString() + "/" + note.getName() + FILE_EXTENSION);
    }

    public boolean noteExists(String name) {
        List<Note> notes = getNotes();
        for (int x = 0; x < notes.size(); x++) {
            if (notes.get(x).getName().equals(name))
                return true;
        }
        return false;
    }

    private File getNoteFile(String name) {
        return new File(getNotesDirectory().toString() + "/" + name + FILE_EXTENSION);
    }

    private String getNoteName(File note) {
        return note.toString().substring(getNotesDirectory().toString().length() + "/".length(), (int) note.toString().length() - FILE_EXTENSION.length());
    }

    private Date getNoteDate(File note) {
        if(!note.exists()) {
            return null;
        }
        return new Date(note.lastModified());
    }

    private String getNotePath(File note) {
        if(!note.exists()) {
            return "";
        }
        return note.getPath();
    }

    private String getNotePreview(File note) {
        if(!note.exists()) {
            return "";
        }
        return NoteUtils.getPreview(getNoteContents(note));
    }


    /*--- Setup & Utility Methods ---*/

    public boolean createNotesDirectory() {
        File directory = getNotesDirectory();

        if(directory == null)
            return false;

        if(!directory.exists()) {
            if(!directory.mkdir())
                return false;
            else
                populateNotesDirectory();
            return true;
        } else {
            if(notesDirectoryIsEmpty())
                populateNotesDirectory();
            return true;
        }
    }

    public boolean notesDirectoryIsEmpty() {
        return (getNotesDirectory().listFiles().length == 0);
    }

    public File getNotesDirectory() {
        File tempDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if(tempDirectory != null) {
            tempDirectory.delete();
            (new File(tempDirectory.toString().substring(0, (int) (tempDirectory.toString().length() - "/DCIM".length())))).delete();
            return new File(tempDirectory.toString().substring(0, (int) (tempDirectory.toString().length() - "/files/DCIM".length())) + "/" + DIRECTORY_NAME);
        } else
            return null;
    }

    private void populateNotesDirectory() {
        writeToNote(DEFAULT_NOTE_NAME, DEFAULT_NOTE_TEXT);

        // Test Data
        writeToNote("Shopping List", "- Bread\n- Milk\n- Eggs\n- Sugar");
        writeToNote("Beautiful Vistas", "- Shipyard, Halo Reach\n- All of Skyrim\n- Winterfell, The Forest");
        writeToNote("Reasons To Code", "- Puzzle Solving!\n- Free To Learn\n- Hones Critical Thinking\n- It Pays Well");
        writeToNote("Cities to Visit", "- Chicago\n- New York\n- Seattle\n- San Francisco");
        writeToNote("A Final Note", "Building this app has been challenging. There were more than a few moments when I was ready to throw in the towel and accept that I'd learned enough. But each successfully rendered list item, formatted to responsive perfection has been a reminder of why I started: to create something beautiful. The world deserves a clean notes app for Android. It's about time someone put in the hours to code one.");
    }
}
