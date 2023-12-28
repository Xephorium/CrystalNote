package com.xephorium.crystalnote.ui.about

import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface AboutContract {

    interface View : BaseView {
        fun showNavigationDrawer()
        fun navigateToDeveloperSite()
        fun navigateToPrivacyPolicy()
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun handleMenuButtonClick()
        abstract fun handleDeveloperButtonClick()
        abstract fun handlePrivacyPolicyButtonClick()
    }
}
