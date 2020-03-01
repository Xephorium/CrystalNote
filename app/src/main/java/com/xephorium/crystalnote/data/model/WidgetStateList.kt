package com.xephorium.crystalnote.data.model

import com.xephorium.crystalnote.data.model.WidgetState.Companion.TextSize
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency
import java.lang.StringBuilder

/*
  WidgetStateList                                             02.27.2020
  Christopher Cruzen

    The WidgetStateList class represents a list of WidgetState objects.
  Its primary purpose is to encapsulate the logic of converting this list
  to and from a string to be stored in Shared Preferences. The string's
  format is as follows:

    "<widgetState1.toString()>, <widgetState2.toString()>, ..."
*/

class WidgetStateList(string: String) {


    /*--- Variable Declarations ---*/

    private var widgetStates: MutableList<WidgetState> = mutableListOf()


    /*--- Constructor ---*/

    init {
        parseString(string)
    }


    /*--- Public Methods ---*/

    fun setNoteIdForWidget(widgetId: Int, noteId: Int) {
        if (containsWidget(widgetId)) {

            // Update Existing Widget
            (widgetStates.find { it.widgetId == widgetId })?.noteId = noteId

        } else {

            // Add Widget
            widgetStates.add(WidgetState(widgetId = widgetId, noteId = noteId))
        }
    }

    fun getNoteIdForWidget(widgetId: Int): Int? {
        return (widgetStates.find { it.widgetId == widgetId })?.noteId
    }

    fun getWidgetState(widgetId: Int): WidgetState? {
        return widgetStates.find { it.widgetId == widgetId }
    }

    fun removeWidgetState(widgetId: Int) {
        if (containsWidget(widgetId)) {
            val index = widgetStates.indexOfFirst { it.widgetId == widgetId }
            widgetStates.removeAt(index)
        }
    }

    fun getWidgetStates(): MutableList<WidgetState> {
        return widgetStates
    }

    fun setTextSizeAtIndex(index: Int, textSize: TextSize) {
        widgetStates[index].textSize = textSize
    }

    fun setTransparencyAtIndex(index: Int, transparency: Transparency) {
        widgetStates[index].transparency = transparency
    }

    fun setBackgroundColorAtIndex(index: Int, color: Int) {
        widgetStates[index].backgroundColor = color
    }

    fun setTitleColorAtIndex(index: Int, color: Int) {
        widgetStates[index].titleColor = color
    }

    fun setTextColorAtIndex(index: Int, color: Int) {
        widgetStates[index].textColor = color
    }

    fun updateWidgetId(oldId: Int, newId: Int) {
        (widgetStates.find { it.widgetId == oldId })?.widgetId = newId
    }

    override fun toString() : String {
        val builder = StringBuilder()
        for (x in 0 until (widgetStates.size - 1)) {
            builder.append(widgetStates[x])
            builder.append(STRING_DELIMITER)
            builder.append(" ")
        }
        if(widgetStates.size > 0) builder.append(widgetStates[widgetStates.size - 1])

        return builder.toString()
    }


    /*--- Private Methods ---*/

    private fun containsWidget(id: Int): Boolean {
        return widgetStates.any { it.widgetId == id}
    }

    private fun parseString(string: String) {

        // Split Into Individual Widget State Substrings
        val states = string.split(Regex(STRING_DELIMITER)).map { it.trim() }

        // Store WidgetStates
        for (state in states) {
            if (state.isNotBlank()) {
                widgetStates.add(WidgetState(state))
            }
        }
    }


    /*--- Constants ---*/

    companion object {
        const val STRING_DELIMITER = ","
    }
}