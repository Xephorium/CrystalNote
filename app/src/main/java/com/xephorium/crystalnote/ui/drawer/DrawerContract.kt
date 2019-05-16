package com.xephorium.crystalnote.ui.drawer

import com.xephorium.crystalnote.data.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView
import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerButton

interface DrawerContract {

    interface View : BaseView {
        fun closeDrawerAfterDelay()

        fun navigateToHome()
        fun navigateToWidget()
        fun navigateToSettings()
        fun navigateToAbout()
        fun setSelectedMenuButton(button: DrawerButton)
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        abstract fun handleHomeClick()
        abstract fun handleWidgetClick()
        abstract fun handleSettingsClick()
        abstract fun handleAboutClick()
    }
}
