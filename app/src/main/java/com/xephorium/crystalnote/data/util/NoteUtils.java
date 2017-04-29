package com.xephorium.crystalnote.data.util;

import android.graphics.Color;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.data.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class NoteUtils {

    private static final String RESERVED_CHARACTERS = "|\\?*<\":>+[]/'";
    private static final int PREVIEW_LENGTH = 50;

    public enum SortType {
        DATE_OLD,
        DATE_NEW,
        NAME_APLHA,
        NAME_REVERSE_APLHA
    }

    public static Note getNoteFromList(List<Note> noteList, String noteName) {
        if (noteList != null && noteList.size() > 0) {
            for (int x = 0; x < noteList.size(); x++) {
                Note note = noteList.get(x);
                if (note.getName().equals(noteName))
                    return note;
            }
        }
        return null;
    }

    public static List<Note> sortNotes(List<Note> inputList, SortType type) {
        List<Note> outputList;

        switch (type) {
            case DATE_NEW:
                outputList = sortNotesByDateNew(inputList);
                break;

            case DATE_OLD:
                outputList = sortNotesByDateOld(inputList);
                break;

            default:
                outputList = new ArrayList<Note>();
        }
        return outputList;
    }

    public static String getFormattedDate(Note note) {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("M.d.yy", Locale.US);
        SimpleDateFormat hourFormat = new SimpleDateFormat("h:mma", Locale.US);

        if (currentDate.getDay() == note.getDate().getDay()) {
            return hourFormat.format(note.getDate()).toLowerCase();
        } else {
            return dayFormat.format(note.getDate());
        }
    }

    public static String getPreview(String contents) {
        contents = contents.replaceAll("\\n", " ");

        if (contents.length() >= PREVIEW_LENGTH) {
            return contents.substring(0, PREVIEW_LENGTH);
        } else {
            return contents;
        }
    }

    public static int getDefaultColor() {
        return Color.parseColor("#aaaaaa");
    }

    public static int getRandomColor() {
        int[] colors = {
                Color.parseColor("#e91e63"),
                Color.parseColor("#f44336"),
                Color.parseColor("#673ab7"),
                Color.parseColor("#3f51b5"),
                Color.parseColor("#2196f3"),
                Color.parseColor("#4caf50"),
                Color.parseColor("#009688"),
                Color.parseColor("#ff5722"),
                Color.parseColor("#795548"),
                Color.parseColor("#9e9e9e"),
                Color.parseColor("#607d8b")};
        return colors[(new Random()).nextInt(colors.length - 1)];
    }

    public static boolean isValidNoteName(String name) {
        if ("".equals(name))
            return false;
        for (int x = 0; x < RESERVED_CHARACTERS.length(); x++) {
            if (name.contains("" + RESERVED_CHARACTERS.charAt(x))) {
                return false;
            }
        }
        return true;
    }

    private static List<Note> sortNotesByDateNew(List<Note> inputList) {
        for (int x = 0; x < inputList.size(); x++) {
            for (int y = inputList.size() -1; y > x; y--) {
                if (inputList.get(y).getDate().after(inputList.get(y - 1).getDate())) {
                    Note temp = inputList.get(y);
                    inputList.set(y, inputList.get(y - 1));
                    inputList.set(y - 1, temp);
                }
            }
        }
        return inputList;
    }

    private static List<Note> sortNotesByDateOld(List<Note> inputList) {
        for (int x = 0; x < inputList.size(); x++) {
            for (int y = inputList.size() -1; y > x; y--) {
                if (inputList.get(y).getDate().before(inputList.get(y - 1).getDate())) {
                    Note temp = inputList.get(y);
                    inputList.set(y, inputList.get(y - 1));
                    inputList.set(y - 1, temp);
                }
            }
        }
        return inputList;
    }
}
