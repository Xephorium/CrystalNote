package com.xephorium.crystalnote.ui.settings


class SettingsPresenter : SettingsContract.Presenter() {


    /*--- Lifecycle Methods ---*/


    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }


    /*--- Private Methods ---*/

}
