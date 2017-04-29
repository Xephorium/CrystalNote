package com.xephorium.crystalnote.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.xephorium.crystalnote.data.model.Note;

/*
  SharedPreferencesManager.java                                             04.13.2017
  Christopher Cruzen

    Convenience wrapper for SharedPreferences io.
*/

public final class SharedPreferencesManager {

    public static final String APP_PRIMARY_KEY = "CrystalNotePreferences";
    public static final String DISPLAY_NOTE_NAME = "DisplayNoteName";

    public static void setDisplayNoteName(Context context, Note note) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(DISPLAY_NOTE_NAME, note.getName());
        editor.apply();
    }

    public static String getDisplayNoteName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE);
        String displayNoteName = prefs.getString(DISPLAY_NOTE_NAME, null);

        if (displayNoteName != null)
            return displayNoteName;

        return null;
    }
}
