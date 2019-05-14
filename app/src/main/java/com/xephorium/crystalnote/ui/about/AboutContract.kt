package com.xephorium.crystalnote.ui.about

import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface AboutContract {

    interface View : BaseView {
        fun showNavigationDrawer()
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun handleMenuButtonClick()
    }
}
