package com.xephorium.crystalnote.ui.widget

import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface WidgetContract {

    interface View : BaseView {
        fun showNavigationDrawer()
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun handleMenuButtonClick()
    }
}
