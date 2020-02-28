package com.xephorium.crystalnote.data.model

import com.xephorium.crystalnote.data.model.Note.Companion.NO_COLOR
import java.lang.StringBuilder

/*
  WidgetState                                              02.28.2020
  Christopher Cruzen

    The WidgetState class represents a single CrystalNote Widget's
  configuration, including the following:

    widgetId                 (Int)      ID for widget
    noteId                   (Int)      ID for note to display
    textSize                 (Int)      Size of display text
    backgroundTransparency   (Double)   Transparency of widget background
    backgroundColor          (Int)      Color of widget background
    titleColor               (Int)      Color of widget title
    textColor                (Int)      Color of widget text

    WidgetState's primary purpose is to encapsulate the logic of
  converting the class' fields to and from a string to be stored
  in Shared Preferences. The string's format is as follows:

    "<widgetId> <noteId> <textSize> <backgroundTransparency> "
      + "<backgroundColor> <titleColor> <textColor>"
*/

class WidgetState(string: String) {


    /*--- Variable Declarations ---*/

    var widgetId: Int = Note.NO_NOTE
        private set
    var noteId: Int = Note.NO_NOTE
    var textSize: Int = 16 // TODO - Replace
    var backgroundTransparency: Double = 1.0
    var backgroundColor: Int = NO_COLOR
    var titleColor: Int = NO_COLOR
    var textColor: Int = NO_COLOR


    /*--- Constructors ---*/

    constructor(
        widgetId: Int,
        noteId: Int = Note.NO_NOTE,
        textSize: Int = 16, // TODO - Replace
        backgroundTransparency: Double = 1.0,
        backgroundColor: Int = NO_COLOR,
        titleColor: Int = NO_COLOR,
        textColor: Int = NO_COLOR
    ): this(widgetId.toString() + STRING_DELIMITER
            + noteId.toString() + STRING_DELIMITER
            + textSize.toString() + STRING_DELIMITER
            + backgroundTransparency.toString() + STRING_DELIMITER
            + backgroundColor.toString() + STRING_DELIMITER
            + titleColor.toString() + STRING_DELIMITER
            + textColor.toString() + STRING_DELIMITER
    )

    init {
        parseString(string)
    }


    /*--- Public Methods ---*/

    override fun toString() : String {
        val builder = StringBuilder()
        builder.append(widgetId)
        builder.append(STRING_DELIMITER)
        builder.append(noteId)
        builder.append(STRING_DELIMITER)
        builder.append(textSize)
        builder.append(STRING_DELIMITER)
        builder.append(backgroundTransparency)
        builder.append(STRING_DELIMITER)
        builder.append(backgroundColor)
        builder.append(STRING_DELIMITER)
        builder.append(titleColor)
        builder.append(STRING_DELIMITER)
        builder.append(textColor)

        return builder.toString()
    }


    /*--- Private Methods ---*/

    fun parseString(string: String) {
        val input = string.trim()
        if (input.isNotBlank()) {
            val values = input.split(Regex(STRING_DELIMITER))
            widgetId = values[0].toInt()
            noteId = values[1].toInt()
            textSize = values[2].toInt()
            backgroundTransparency = values[3].toDouble()
            backgroundColor = values[4].toInt()
            titleColor = values[5].toInt()
            textColor = values[6].toInt()
        }
    }


    /*--- Constants ---*/

    companion object {
        const val STRING_DELIMITER = " "
    }
}