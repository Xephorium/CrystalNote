package com.xephorium.crystalnote.data.model

import java.lang.StringBuilder

/*
  WidgetState                                              02.28.2020
  Christopher Cruzen

    The WidgetState class represents a single CrystalNote Widget's
  configuration, including the following:

    widgetId                 (Int)     ID for widget
    noteId                   (Int)     ID for note to display
    textSize                 (Int)     Size of display text
    backgroundTransparency   (Float)   Transparency of widget background
    backgroundColor          (Int)     Color of widget background
    titleColor               (Int)     Color of widget title
    textColor                (Int)     Color of widget text

    WidgetState's primary purpose is to encapsulate the logic of
  converting the class' fields to and from a string to be stored
  in Shared Preferences. The string's format is as follows:

    "<widgetId> <noteId> <textSize> <backgroundTransparency> "
      + "<backgroundColor> <titleColor> <textColor>"
*/

class WidgetState(string: String) {


    /*--- Variable Declarations ---*/

    private var widgetId: Int = Note.DEFAULT_NOTE_ID
    private var noteId: Int = Note.DEFAULT_NOTE_ID


    /*--- Constructor ---*/

    constructor(
        widgetId: Int,
        noteId: Int = Note.DEFAULT_NOTE_ID
    ): this(widgetId.toString() + " "
            + noteId.toString()
    )

    init {
        parseString(string)
    }


    /*--- Public Methods ---*/

    fun getWidgetId(): Int {
        return widgetId
    }

    fun getNoteId(): Int {
        return noteId
    }

    fun setNoteId(id: Int) {
        noteId = id
    }

    override fun toString() : String {
        val builder = StringBuilder()
        builder.append(widgetId)
        builder.append(" ")
        builder.append(noteId)

        return builder.toString()
    }


    /*--- Private Methods ---*/

    fun parseString(string: String) {
        val input = string.trim()
        if (input.isNotBlank()) {
            val values = input.split(Regex(" "))
            widgetId = values[0].toInt()
            noteId = values[1].toInt()
        }
    }
}