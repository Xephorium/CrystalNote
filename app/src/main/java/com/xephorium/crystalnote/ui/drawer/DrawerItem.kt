package com.xephorium.crystalnote.ui.drawer

data class DrawerItem(
        val iconResource: Int,
        val text: String,
        val listener: ClickListener,
        val type: DrawerItemType) {

    interface ClickListener {
        fun onClick()
    }

    companion object {
        enum class DrawerItemType {
            BUTTON,
            DIVIDER
        }

        enum class DrawerButton(val displayName: String) {
            NOTES("Notes"),
            SETTINGS("Settings"),
            WIDGET("Widget"),
            ABOUT("About")
        }
    }
}