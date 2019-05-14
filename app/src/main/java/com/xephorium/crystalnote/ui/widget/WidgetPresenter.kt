package com.xephorium.crystalnote.ui.widget


class WidgetPresenter : WidgetContract.Presenter() {


    /*--- Lifecycle Methods ---*/


    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }


    /*--- Private Methods ---*/

}
