package com.xephorium.crystalnote.ui.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardUtils {

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    public static int getKeyboardHeight(Activity activity) {
        int appHeight = DisplayUtils.getAppHeight(activity);
        int displayHeight = DisplayUtils.getDisplayHeight(activity);
        int statusBarHeight = DisplayUtils.getStatusBarHeight(activity);
        return displayHeight - (appHeight + statusBarHeight);
    }
}
