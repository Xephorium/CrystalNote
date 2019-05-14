package com.xephorium.crystalnote.ui.settings

import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface SettingsContract {

    interface View : BaseView {
        fun showNavigationDrawer()
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun handleMenuButtonClick()
    }
}
