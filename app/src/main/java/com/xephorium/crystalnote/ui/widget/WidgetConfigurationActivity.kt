package com.xephorium.crystalnote.ui.widget

import android.app.Activity
import android.os.Bundle
import android.appwidget.AppWidgetManager
import android.content.Intent
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository

/*
  WidgetConfigurationActivity                                  03.04.2020
  Christopher Cruzen

    WidgetConfigurationActivity is a skeleton activity that exists solely
  to navigate around Widgets' lack of onCreate() method. This activity is
  instantiated once with a transparent splash screen and no layout each
  time a new widget is added to the user's home screen. It records the new
  widgetId in SharedPreferences, fires a success intent, and closes.
*/


class WidgetConfigurationActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {

            // Retrieve New Widget ID
            val widgetId = it.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )

            // Store New Widget ID
            SharedPreferencesRepository(this).createNewWidgetState(widgetId)

            // Fire Successful Widget Configuration Intent & Finish
            val resultIntent = Intent()
            resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}