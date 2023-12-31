package com.xephorium.crystalnote.data.model

import android.graphics.Color
import java.lang.StringBuilder

/*
  WidgetState                                              02.28.2020
  Christopher Cruzen

    The WidgetState class represents a single CrystalNote Widget's
  configuration, including the following:

    widgetId            (Int)        ID for widget
    noteId              (Int)        ID for note to display
    backgroundColor     (Int)        Color of widget background
    titleColor          (Int)        Color of widget title text
    contentColor        (Int)        Color of widget content text
    textSize            (Int)        Size of widget text
    backgroundAlpha     (Double)     Transparency of widget background
    contentAlpha        (Double)     Transparency of widget content

    WidgetState's primary purpose is to encapsulate the logic of
  converting the class' fields to and from a string to be stored
  in Shared Preferences. The string's format is as follows:

    "<widgetId> <noteId> <backgroundColor> <titleColor> <contentColor>"
      + "<textSize> <backgroundAlpha> <contentAlpha>"
*/

class WidgetState(string: String) {


    /*--- Variable Declarations ---*/

    var widgetId: Int = Note.NO_NOTE
    var noteId: Int = Note.NO_NOTE
    var backgroundColor: Int = DEFAULT_BACKGROUND_COLOR
    var titleColor: Int = DEFAULT_TITLE_COLOR
    var contentColor: Int = DEFAULT_CONTENT_COLOR
    var textSize: TextSize = DEFAULT_TEXT_SIZE
    var backgroundAlpha: Transparency = DEFAULT_TRANSPARENCY
    var contentAlpha: Transparency = DEFAULT_TRANSPARENCY
    var cornerCurve: CornerCurve = DEFAULT_CORNER_CURVE

    /*--- Constructors ---*/

    constructor(
        widgetId: Int,
        noteId: Int = Note.NO_NOTE,
        backgroundColor: Int = DEFAULT_BACKGROUND_COLOR,
        titleColor: Int = DEFAULT_TITLE_COLOR,
        contentColor: Int = DEFAULT_CONTENT_COLOR,
        textSize: TextSize = DEFAULT_TEXT_SIZE,
        backgroundAlpha: Transparency = DEFAULT_TRANSPARENCY,
        contentAlpha: Transparency = DEFAULT_TRANSPARENCY,
        cornerCurve: CornerCurve = DEFAULT_CORNER_CURVE
    ): this(widgetId.toString() + STRING_DELIMITER
            + noteId.toString() + STRING_DELIMITER
            + backgroundColor.toString() + STRING_DELIMITER
            + titleColor.toString() + STRING_DELIMITER
            + contentColor.toString() + STRING_DELIMITER
            + textSize.size.toString() + STRING_DELIMITER
            + backgroundAlpha.value.toString() + STRING_DELIMITER
            + contentAlpha.value.toString() + STRING_DELIMITER
            + cornerCurve.value.toString()
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
        builder.append(backgroundColor)
        builder.append(STRING_DELIMITER)
        builder.append(titleColor)
        builder.append(STRING_DELIMITER)
        builder.append(contentColor)
        builder.append(STRING_DELIMITER)
        builder.append(textSize.size)
        builder.append(STRING_DELIMITER)
        builder.append(backgroundAlpha.value)
        builder.append(STRING_DELIMITER)
        builder.append(contentAlpha.value)
        builder.append(STRING_DELIMITER)
        builder.append(cornerCurve.value)

        return builder.toString()
    }


    /*--- Private Methods ---*/

    private fun parseString(string: String) {
        val input = string.trim()
        if (input.isNotBlank()) {
            val values = input.split(Regex(STRING_DELIMITER))
            widgetId = values[0].toInt()
            noteId = values[1].toInt()
            backgroundColor = values[2].toInt()
            titleColor = values[3].toInt()
            contentColor = values[4].toInt()
            textSize = TextSize.fromSize(values[5].toInt())
            backgroundAlpha = Transparency.fromValue(values[6].toDouble())
            contentAlpha = Transparency.fromValue(values[7].toDouble())

            // Accommodate Old WidgetState Without CornerCurve
            cornerCurve = if (values.size > 8) CornerCurve.fromValue(values[8].toDouble())
            else DEFAULT_CORNER_CURVE
        }
    }


    /*--- Constants ---*/

    companion object {
        const val STRING_DELIMITER = " "
        val DEFAULT_BACKGROUND_COLOR = Color.parseColor("#FFFFFF")
        val DEFAULT_TITLE_COLOR = Color.parseColor("#363636")
        val DEFAULT_CONTENT_COLOR = Color.parseColor("#666666")
        val DEFAULT_TEXT_SIZE: TextSize = TextSize.Medium
        val DEFAULT_TRANSPARENCY = Transparency.NONE
        val DEFAULT_CORNER_CURVE = CornerCurve.Medium

        enum class TextSize(val displayName: String, val size: Int) {
            Tiny("Tiny", 12),
            Small("Small", 13),
            Medium("Medium", 14),
            Large("Large", 15),
            Huge("Huge", 16);

            companion object {
                fun fromSize(size: Int): TextSize {
                    return entries.firstOrNull { it.size == size } ?: Medium
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
                    return entries.firstOrNull { it.value == value } ?: NONE
                }
            }
        }

        enum class CornerCurve(val displayName: String, val value: Double) {
            None("None", 0.0),
            Small("Small", 4.0),
            Medium("Medium", 8.0),
            Large("Large", 12.0),
            Huge("Huge", 16.0);

            companion object {
                fun fromValue(value: Double): CornerCurve {
                    return entries.firstOrNull { it.value == value } ?: None
                }
            }
        }
    }
}