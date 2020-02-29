package com.xephorium.crystalnote.data.model

import android.graphics.Color
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
    var textSize: TextSize = DEFAULT_TEXT_SIZE
    var backgroundTransparency: Transparency = DEFAULT_BACKGROUND_TRANSPARENCY
    var backgroundColor: Int = DEFAULT_BACKGROUND_COLOR
    var titleColor: Int = DEFAULT_TITLE_COLOR
    var textColor: Int = DEFAULT_TEXT_COLOR


    /*--- Constructors ---*/

    constructor(
        widgetId: Int,
        noteId: Int = Note.NO_NOTE,
        textSize: TextSize = DEFAULT_TEXT_SIZE,
        backgroundTransparency: Transparency = DEFAULT_BACKGROUND_TRANSPARENCY,
        backgroundColor: Int = DEFAULT_BACKGROUND_COLOR,
        titleColor: Int = DEFAULT_TITLE_COLOR,
        textColor: Int = DEFAULT_TEXT_COLOR
    ): this(widgetId.toString() + STRING_DELIMITER
            + noteId.toString() + STRING_DELIMITER
            + textSize.size.toString() + STRING_DELIMITER
            + backgroundTransparency.value.toString() + STRING_DELIMITER
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
        builder.append(textSize.size)
        builder.append(STRING_DELIMITER)
        builder.append(backgroundTransparency.value)
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
            textSize = TextSize.fromSize(values[2].toInt())
            backgroundTransparency = Transparency.fromValue(values[3].toDouble())
            backgroundColor = values[4].toInt()
            titleColor = values[5].toInt()
            textColor = values[6].toInt()
        }
    }


    /*--- Constants ---*/

    companion object {
        const val STRING_DELIMITER = " "
        val DEFAULT_TEXT_SIZE: TextSize = TextSize.Medium
        val DEFAULT_BACKGROUND_TRANSPARENCY = Transparency.NONE
        val DEFAULT_BACKGROUND_COLOR = Color.parseColor("#FFFFFF")
        val DEFAULT_TITLE_COLOR = Color.parseColor("#1C1C1C")
        val DEFAULT_TEXT_COLOR = Color.parseColor("#666666")

        enum class TextSize(val displayName: String, val size: Int) {
            Small("Small", 13),
            Medium("Medium", 14),
            Large("Large", 15),
            ExtraLarge("Extra Large", 16);

            companion object {
                fun fromSize(size: Int): TextSize {
                    return values().firstOrNull { it.size == size } ?: Medium
                }
            }
        }

        enum class Transparency(val displayName: String, val value: Double) {
            NONE("None", 0.0),
            Low("20 %", 0.2),
            Medium("40 %", 0.4),
            High("60 %", 0.6),
            Higher("80 %", 0.8),
            Full("100 %", 1.0);

            companion object {
                fun fromValue(value: Double): Transparency {
                    return values().firstOrNull { it.value == value } ?: NONE
                }
            }
        }
    }
}