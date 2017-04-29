package com.xephorium.crystalnote.ui.home;

import android.content.Context;

import com.xephorium.crystalnote.data.NoteManager;
import com.xephorium.crystalnote.ui.base.BasePresenter;

public class HomePresenter extends BasePresenter<HomeView> {

    private Context context;
    private NoteManager noteManager;

    public HomePresenter(HomeView view) {
        attachView(view);
        this.context = (Context) view;
        this.noteManager = new NoteManager(context);
    }
}
