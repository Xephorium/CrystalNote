package com.xephorium.crystalnote.ui.widget

import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.View
import android.widget.RemoteViews

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.select.SelectActivity
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import com.xephorium.crystalnote.data.model.WidgetState
import com.xephorium.crystalnote.data.utility.ColorUtility
import com.xephorium.crystalnote.data.utility.CrystalNoteLogger
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity.Companion.KEY_LAUNCH_FROM_WIDGET
import com.xephorium.crystalnote.ui.update.UpdateNoteActivity.Companion.KEY_NOTE_ID


/*
  NotesWidgetProvider                                      05.11.2019
  Christopher Cruzen

    Defines appearance and view behavior for all CrystalNote widgets.
*/

class NotesWidgetProvider : AppWidgetProvider() {


    /*--- Lifecycle Methods ---*/

    // Handle Widget Update
    override fun onUpdate(
        context: Context,
        widgetManager: AppWidgetManager,
        widgetIdsPendingUpdate: IntArray
    ) {

        // Create Update Variables
        val sharedPreferencesRepository = SharedPreferencesRepository(context)

        widgetIdsPendingUpdate.let { widgetIds ->

            // For Each Widget
            for (widgetId in widgetIds) {

                // Get Widget Variables
                val widgetView = RemoteViews(context.packageName, R.layout.note_widget_layout)
                val widgetState = sharedPreferencesRepository.getWidgetState(widgetId)
                val widgetNoteId = sharedPreferencesRepository.getNoteIdForWidget(widgetId)
                val widgetNote = widgetNoteId?.let { id ->
                        val callable = Callable {
                            NoteRoomRepository(context).getNoteSynchronously(id)
                        }
                        val future = Executors.newSingleThreadExecutor().submit(callable)
                        future.get()
                    }

                // Set Widget Style
                if (widgetState == null) {
                    styleWidget(widgetView, WidgetState(0, 0))
                } else {
                    styleWidget(widgetView, widgetState)
                }

                // Populate Widget Fields
                if (widgetNote != null) {

                    if (widgetNote.password.isEmpty()) {

                        // Populate Normal State
                        widgetView.setViewVisibility(R.id.textWidgetTitle, View.VISIBLE)
                        widgetView.setViewVisibility(R.id.textWidgetContent, View.VISIBLE)
                        widgetView.setViewVisibility(R.id.textWidgetEmpty, View.GONE)
                        widgetView.setViewVisibility(R.id.textWidgetLocked, View.GONE)
                        widgetView.setTextViewText(R.id.textWidgetTitle, widgetNote.name)
                        widgetView.setTextViewText(R.id.textWidgetContent, widgetNote.contents)


                    } else {

                        // Populate Locked State
                        widgetView.setViewVisibility(R.id.textWidgetTitle, View.GONE)
                        widgetView.setViewVisibility(R.id.textWidgetContent, View.GONE)
                        widgetView.setViewVisibility(R.id.textWidgetEmpty, View.GONE)
                        widgetView.setViewVisibility(R.id.textWidgetLocked, View.VISIBLE)
                    }

                } else {

                    // Populate Empty State
                    widgetView.setViewVisibility(R.id.textWidgetTitle, View.GONE)
                    widgetView.setViewVisibility(R.id.textWidgetContent, View.GONE)
                    widgetView.setViewVisibility(R.id.textWidgetEmpty, View.VISIBLE)
                    widgetView.setViewVisibility(R.id.textWidgetLocked, View.GONE)
                }

                // Set Listeners
                widgetView.setOnClickPendingIntent(
                        R.id.textWidgetTitle,
                        getOnClickPendingIntent(context, TITLE_CLICK_INTENT, widgetId)
                )
                widgetView.setOnClickPendingIntent(
                        R.id.textWidgetContent,
                        getOnClickPendingIntent(context, TEXT_CLICK_INTENT, widgetId)
                )
                widgetView.setOnClickPendingIntent(
                        R.id.textWidgetEmpty,
                        getOnClickPendingIntent(context, EMPTY_CLICK_INTENT, widgetId)
                )

                widgetManager.updateAppWidget(widgetId, widgetView)
            }
        }
    }

    // Handle Intents Generated by Widget Interaction
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // Create Receive Variables
        val widgetId = intent.getIntExtra(KEY_WIDGET_ID, 0)

        when (intent.action) {

            TITLE_CLICK_INTENT, TEXT_CLICK_INTENT -> {

                // Launch UpdateActivity for Note
                SharedPreferencesRepository(context).getNoteIdForWidget(widgetId)?.let { id ->
                    val updateIntent = Intent(context, UpdateNoteActivity::class.java)
                    updateIntent.putExtra(KEY_NOTE_ID, id)
                    updateIntent.putExtra(KEY_LAUNCH_FROM_WIDGET, true)
                    updateIntent.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(updateIntent)
                }
            }

            EMPTY_CLICK_INTENT -> {

                // Choose New Note
                val buttonIntent = Intent(context, SelectActivity::class.java)
                buttonIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                buttonIntent.putExtra(KEY_WIDGET_ID, widgetId)
                context.startActivity(buttonIntent)
            }
        }
    }

    // Handle Widget Delete
    override fun onDeleted(context: Context, widgetIds: IntArray) {
        super.onDeleted(context, widgetIds)

        // Remove Widget ID from Shared Preferences
        val sharedPreferencesRepository = SharedPreferencesRepository(context)
        for (widgetId in widgetIds) {
            sharedPreferencesRepository.removeWidgetState(widgetId)
        }
    }

    // Handle Final Widget Deletion
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        SharedPreferencesRepository(context).removeAllWidgetStates()
    }

    // Handle Device Reboot
    override fun onRestored(context: Context, oldWidgetIds: IntArray, newWidgetIds: IntArray) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)

        // Create Update Variables
        val sharedPreferencesRepository = SharedPreferencesRepository(context)
        val widgetStateList = sharedPreferencesRepository.getWidgetStateList()

        // Update Widget ID's
        for (index in oldWidgetIds.indices) {
            widgetStateList.updateWidgetId(oldWidgetIds[index], newWidgetIds[index])
        }
        sharedPreferencesRepository.setWidgetStateList(widgetStateList)
    }


    /*--- Private Methods ---*/

    private fun styleWidget(widgetView: RemoteViews, state: WidgetState) {

        // Background Color
        widgetView.setInt(
            R.id.imageWidgetBackground,
            "setColorFilter",
            state.backgroundColor
        )

        // Background Transparency
        widgetView.setInt(
            R.id.imageWidgetBackground,
            "setImageAlpha",
            ((1.0 - state.backgroundAlpha.value) * 255.0).toInt()
        )

        // Text Size
        widgetView.setTextViewTextSize(
            R.id.textWidgetTitle,
            COMPLEX_UNIT_SP,
            (state.textSize.size + 1).toFloat()
        )
        widgetView.setTextViewTextSize(
            R.id.textWidgetContent,
            COMPLEX_UNIT_SP,
            (state.textSize.size).toFloat()
        )
        widgetView.setTextViewTextSize(
            R.id.textWidgetEmpty,
            COMPLEX_UNIT_SP,
            (state.textSize.size).toFloat()
        )
        widgetView.setTextViewTextSize(
            R.id.textWidgetLocked,
            COMPLEX_UNIT_SP,
            (state.textSize.size).toFloat()
        )

        // Text Color
        widgetView.setTextColor(R.id.textWidgetTitle, state.titleColor)
        widgetView.setTextColor(
            R.id.textWidgetContent,
            ColorUtility.applyTransparency(state.contentColor, state.contentAlpha)
        )
        widgetView.setTextColor(
            R.id.textWidgetEmpty,
            ColorUtility.applyTransparency(state.contentColor, state.contentAlpha)
        )
        widgetView.setTextColor(
            R.id.textWidgetLocked,
            ColorUtility.applyTransparency(state.contentColor, state.contentAlpha)
        )
    }

    private fun getOnClickPendingIntent(
        context: Context,
        intentAction: String,
        widgetId: Int
    ): PendingIntent {
        val intent = Intent(context, this.javaClass)
        intent.action = intentAction
        intent.putExtra(KEY_WIDGET_ID, widgetId)
        return PendingIntent.getBroadcast(
            context,
            widgetId,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun log(context: Context, string: String) {
        CrystalNoteLogger.log(context, string)
    }


    /*--- Constants ---*/

    companion object {
        private const val TITLE_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.TITLE"
        private const val TEXT_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.TEXT"
        private const val EMPTY_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.EMPTY"
        const val KEY_WIDGET_ID = "WIDGET_ID_KEY"

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