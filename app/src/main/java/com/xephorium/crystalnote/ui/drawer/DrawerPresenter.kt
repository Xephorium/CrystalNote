package com.xephorium.crystalnote.ui.drawer

import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerButton.*
import android.os.Handler

class DrawerPresenter : DrawerContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: DrawerContract.View) {
        super.attachView(view)

        // Set Selected Drawer Button
        val button = sharedPreferencesRepository.getSelectedDrawerButton()
        Handler().postDelayed(
                { button?.let { this.view?.setSelectedMenuButton(it) } },
                DRAWER_BUTTON_SELECT_DELAY
        )
    }


    /*--- Action Handling Methods ---*/

    override fun handleHomeClick() {
        view?.closeDrawerAfterDelay()
        sharedPreferencesRepository.setSelectedDrawerButton(NOTES)
        view?.setSelectedMenuButton(NOTES)
        view?.navigateToHome()
    }

    override fun handleWidgetClick() {
        view?.closeDrawerAfterDelay()
        sharedPreferencesRepository.setSelectedDrawerButton(WIDGET)
        view?.setSelectedMenuButton(WIDGET)
        view?.navigateToWidget()
    }

    override fun handleSettingsClick() {
        view?.closeDrawerAfterDelay()
        sharedPreferencesRepository.setSelectedDrawerButton(SETTINGS)
        view?.setSelectedMenuButton(SETTINGS)
        view?.navigateToSettings()
    }

    override fun handleAboutClick() {
        view?.closeDrawerAfterDelay()
        sharedPreferencesRepository.setSelectedDrawerButton(ABOUT)
        view?.setSelectedMenuButton(ABOUT)
        view?.navigateToAbout()
    }


    /*--- Constants ---*/

    companion object {
        private const val DRAWER_BUTTON_SELECT_DELAY = 250.toLong()
    }
}
