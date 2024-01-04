package com.xephorium.crystalnote.ui.colorpicker.view

enum class ColorPickerTab(val displayName: String) {
    PALETTE("Palette"),
    CUSTOM("Custom");

    companion object {
        fun fromIndex(index: Int): ColorPickerTab = entries[index]
        fun size(): Int = entries.size
    }
}