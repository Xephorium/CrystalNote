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
        if (sharedPreferencesRepository.getSelectedDrawerButton() != NOTES) {
            view?.closeDrawerAfterDelay()
            sharedPreferencesRepository.setSelectedDrawerButton(NOTES)
            view?.setSelectedMenuButton(NOTES)
            view?.navigateToHome()
        } else {
            view?.closeDrawer()
        }
    }

    override fun handleWidgetClick() {
        if (sharedPreferencesRepository.getSelectedDrawerButton() != WIDGET) {
            view?.closeDrawerAfterDelay()
            sharedPreferencesRepository.setSelectedDrawerButton(WIDGET)
            view?.setSelectedMenuButton(WIDGET)
            view?.navigateToWidget()
        } else {
            view?.closeDrawer()
        }
    }

    override fun handleSettingsClick() {
        if (sharedPreferencesRepository.getSelectedDrawerButton() != SETTINGS) {
            view?.closeDrawerAfterDelay()
            sharedPreferencesRepository.setSelectedDrawerButton(SETTINGS)
            view?.setSelectedMenuButton(SETTINGS)
            view?.navigateToSettings()
        } else {
            view?.closeDrawer()
        }
    }

    override fun handleAboutClick() {
        if (sharedPreferencesRepository.getSelectedDrawerButton() != ABOUT) {
            view?.closeDrawerAfterDelay()
            sharedPreferencesRepository.setSelectedDrawerButton(ABOUT)
            view?.setSelectedMenuButton(ABOUT)
            view?.navigateToAbout()
        } else {
            view?.closeDrawer()
        }
    }

    override fun handleBackClick() {
        if (sharedPreferencesRepository.getSelectedDrawerButton() == NOTES) {
            view?.closeCrystalNote()
        } else {
            sharedPreferencesRepository.setSelectedDrawerButton(NOTES)
            view?.navigateToHomeImmediately()
        }
    }


    /*--- Constants ---*/

    companion object {
        private const val DRAWER_BUTTON_SELECT_DELAY = 250.toLong()
    }
}
