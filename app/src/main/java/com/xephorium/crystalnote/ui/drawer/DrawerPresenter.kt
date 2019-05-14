package com.xephorium.crystalnote.ui.drawer


class DrawerPresenter : DrawerContract.Presenter() {


    /*--- Action Handling Methods ---*/

    override fun handleHomeClick() {
        view?.closeDrawerAfterDelay()
        view?.navigateToHome()
    }

    override fun handleWidgetClick() {
        view?.closeDrawerAfterDelay()
        view?.navigateToWidget()
    }

    override fun handleSettingsClick() {
        view?.closeDrawerAfterDelay()
        view?.navigateToSettings()
    }

    override fun handleAboutClick() {
        view?.closeDrawerAfterDelay()
        view?.navigateToAbout()
    }
}
