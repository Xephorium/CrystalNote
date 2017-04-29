package com.xephorium.crystalnote.ui.util;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;

public class DisplayUtils {

    public static int getAppHeight(Activity activity) {
        Rect rect = new Rect();
        View rootView = activity.getWindow().getDecorView();
        rootView.getWindowVisibleDisplayFrame(rect);
        return rect.bottom - rect.top;
    }

    public static int getAppWidth(Activity activity) {
        Rect rect = new Rect();
        View rootView = activity.getWindow().getDecorView();
        rootView.getWindowVisibleDisplayFrame(rect);
        return rect.right - rect.left;
    }

    public static int getDisplayHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getDisplayWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getStatusBarHeight(Activity activity) {
        return activity.getResources().getDimensionPixelSize(
                activity.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static int getAbsoluteViewTop(View view) {
        int[] coordinates = new int[2];
        view.getLocationOnScreen(coordinates);
        return coordinates[1];
    }
}