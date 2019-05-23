package com.xephorium.crystalnote.ui.widget

import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.data.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.ui.select.SelectActivity
import com.xephorium.crystalnote.ui.update.UpdateActivity
import com.xephorium.crystalnote.ui.update.UpdateActivity.Companion.KEY_LAUNCH_FROM_WIDGET
import com.xephorium.crystalnote.ui.update.UpdateActivity.Companion.KEY_NOTE_NAME

/*
  NotesWidgetProvider                          05.11.2019
  Christopher Cruzen

    Defines appearance and view behavior of notes widget.
*/

class NotesWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
    ) {

        getWidgetInstances(context, appWidgetManager)?.let { widgets ->
            for (widgetInstance in widgets) {

                // Get Necessary Variables
                val widgetView = RemoteViews(context.packageName, R.layout.note_widget_layout)
                val sharedPreferencesRepository = SharedPreferencesRepository(context)
                val noteRepository = NoteRepository(context)
                val displayNote = sharedPreferencesRepository.getDisplayNoteName()?.let { name ->
                    NoteUtility.getNoteFromList(noteRepository.getNotes(), name)
                }

                // Populate Fields
                if (displayNote != null) {

                    widgetView.setViewVisibility(R.id.textWidgetTitle, View.VISIBLE)
                    widgetView.setViewVisibility(R.id.textWidgetContent, View.VISIBLE)
                    widgetView.setViewVisibility(R.id.textWidgetEmpty, View.GONE)

                    widgetView.setTextViewText(R.id.textWidgetTitle, displayNote.name)
                    widgetView.setTextViewText(
                            R.id.textWidgetContent,
                            noteRepository.readNoteContents(displayNote.name)
                    )
                } else {
                    widgetView.setViewVisibility(R.id.textWidgetTitle, View.GONE)
                    widgetView.setViewVisibility(R.id.textWidgetContent, View.GONE)
                    widgetView.setViewVisibility(R.id.textWidgetEmpty, View.VISIBLE)
                }

                // Set Listeners
                widgetView.setOnClickPendingIntent(
                        R.id.textWidgetTitle,
                        getOnClickPendingIntent(context, TITLE_CLICK_INTENT)
                )
                widgetView.setOnClickPendingIntent(
                        R.id.textWidgetContent,
                        getOnClickPendingIntent(context, TEXT_CLICK_INTENT)
                )
                widgetView.setOnClickPendingIntent(
                        R.id.textWidgetEmpty,
                        getOnClickPendingIntent(context, EMPTY_CLICK_INTENT)
                )

                appWidgetManager.updateAppWidget(widgetInstance, widgetView)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {

            TITLE_CLICK_INTENT -> {

                // Choose New Display Note
                val buttonIntent = Intent(context, SelectActivity::class.java)
                buttonIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(buttonIntent)
            }

            TEXT_CLICK_INTENT -> {

                // Update Current Display Note
                SharedPreferencesRepository(context).getDisplayNoteName()?.let { name ->
                    val updateIntent = Intent(context, UpdateActivity::class.java)
                    updateIntent.putExtra(KEY_NOTE_NAME, name)
                    updateIntent.putExtra(KEY_LAUNCH_FROM_WIDGET, true)
                    updateIntent.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(updateIntent)
                }
            }

            EMPTY_CLICK_INTENT -> {

                // Choose New Display Note
                val buttonIntent = Intent(context, SelectActivity::class.java)
                buttonIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(buttonIntent)
            }
        }
    }

    private fun getOnClickPendingIntent(context: Context, intentAction: String): PendingIntent {
        val intent = Intent(context, this.javaClass)
        intent.action = intentAction
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    private fun updateWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context.applicationContext)
        val widgetInstances = getWidgetInstances(context, appWidgetManager)

        if (widgetInstances != null && widgetInstances.isNotEmpty())
            onUpdate(context, appWidgetManager, widgetInstances)
    }

    private fun getWidgetInstances(context: Context, appWidgetManager: AppWidgetManager): IntArray? {
        val widgetName = ComponentName(context, this.javaClass)
        return appWidgetManager.getAppWidgetIds(widgetName)
    }

    companion object {
        private const val TITLE_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.TITLE"
        private const val TEXT_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.TEXT"
        private const val EMPTY_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.EMPTY"

        fun refreshWidgets(context: Context, application: Application) {
            val intent = Intent(context, NotesWidgetProvider::class.java)
            val ids = AppWidgetManager.getInstance(application)
                    .getAppWidgetIds(ComponentName(application, NotesWidgetProvider::class.java))

            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(intent)
        }
    }
}