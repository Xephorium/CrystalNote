package com.xephorium.crystalnote.ui.drawer

import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface DrawerContract {

    interface View : BaseView {
        fun closeDrawerAfterDelay()

        fun navigateToHome()
        fun navigateToWidget()
        fun navigateToSettings()
        fun navigateToAbout()
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun handleHomeClick()
        abstract fun handleWidgetClick()
        abstract fun handleSettingsClick()
        abstract fun handleAboutClick()
    }
}
