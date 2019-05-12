package com.xephorium.crystalnote.ui.base

interface Presenter<V : BaseView> {

    fun attachView(view: V)

    fun detachView()
}
