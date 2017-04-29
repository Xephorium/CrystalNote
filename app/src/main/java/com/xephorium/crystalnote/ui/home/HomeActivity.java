package com.xephorium.crystalnote.ui.home;

import android.os.Bundle;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.ui.base.BaseActivity;

import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeView {

    HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        ButterKnife.bind(this);

        presenter = new HomePresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
