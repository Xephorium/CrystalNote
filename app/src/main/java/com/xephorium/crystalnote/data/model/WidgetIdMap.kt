package com.xephorium.crystalnote.data.model

import java.lang.StringBuilder

/*
  WidgetIdMap                                              02.27.2020
  Christopher Cruzen

    The WidgetIdMap class represents a list of CrystalNote WidgetIds
  and their associated noteIds. Its primary purpose is to encapsulate
  the logic of converting this list to and from a string to be stored
  in Shared Preferences. The string's format is as follows:

    "<widgetId0> <noteId0>, <widgetId1> <noteId1>, ..."
*/

class WidgetIdMap(string: String) {


    /*--- Variable Declarations ---*/

    private var widgetIds: MutableList<Int> = mutableListOf()
    private var noteIds: MutableList<Int> = mutableListOf()


    /*--- Constructor ---*/

    init {
        parseString(string)
    }


    /*--- Public Methods ---*/

    fun setNoteIdForWidget(widgetId: Int, noteId: Int) {
        if (widgetIds.contains(widgetId)) {

            // Update Existing Widget
            noteIds[widgetIds.indexOf(widgetId)] = noteId

        } else {

            // Add Widget
            widgetIds.add(widgetId)
            noteIds.add(noteId)
        }
    }

    fun getNoteIdForWidget(widgetId: Int): Int? {
        return if (widgetIds.contains(widgetId)) noteIds[widgetIds.indexOf(widgetId)] else null
    }

    fun removeWidgetId(widgetId: Int) {
        if (widgetIds.contains(widgetId)) {
            val index = widgetIds.indexOf(widgetId)
            widgetIds.removeAt(index)
            noteIds.removeAt(index)

        }
    }

    override fun toString() : String {

        // Add All But Last Pair
        val builder = StringBuilder()
        for (x in 0 until (widgetIds.size - 1)) {
            builder.append(widgetIds[x])
            builder.append(" ")
            builder.append(noteIds[x])
            builder.append(", ")
        }

        // Add Last Pair
        builder.append(widgetIds[widgetIds.size - 1])
        builder.append(" ")
        builder.append(noteIds[noteIds.size - 1])

        return builder.toString()
    }


    /*--- Private Methods ---*/

    fun parseString(string: String) {

        // Split Into Pairs
        val pairs = string.split(Regex(",")).map { it.trim() }

        // Store Identifiers
        for (pair in pairs) {
            if (pair.isNotBlank()) {
                val ids = pair.split(Regex(" "))
                widgetIds.add(ids[0].toInt())
                noteIds.add(ids[1].toInt())
            }
        }
    }
}