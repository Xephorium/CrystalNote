package com.xephorium.crystalnote.ui.base

open class BasePresenter<T : BaseView> : Presenter<T> {

    var view: T? = null
        private set

    val isViewAttached: Boolean
        get() = this.view != null

    override fun attachView(view: T) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }
}
