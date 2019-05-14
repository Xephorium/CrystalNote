package com.xephorium.crystalnote.ui.base.drawer

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
            ITEM,
            DIVIDER
        }
    }
}