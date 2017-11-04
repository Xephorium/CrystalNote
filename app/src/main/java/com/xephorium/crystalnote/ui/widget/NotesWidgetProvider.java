package com.xephorium.crystalnote.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.data.NoteRepository;
import com.xephorium.crystalnote.data.SharedPreferencesManager;
import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.data.util.NoteUtils;
import com.xephorium.crystalnote.ui.IntentLibrary;
import com.xephorium.crystalnote.ui.selection.SelectionActivity;

import java.util.List;

/*
  NotesWidgetProvider.java                     04.15.2017
  Christopher Cruzen

    Defines appearance and view behavior of notes widget.
*/

public class NotesWidgetProvider extends AppWidgetProvider {

    private static final String BUTTON_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.BUTTON";
    private static final String TEXT_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.TEXT";
    private static final String TITLE_CLICK_INTENT = "com.xephorium.crystalnote.widget.click.TITLE";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		int[] widgetInstances = getWidgetInstances(context, appWidgetManager);

		for (int widgetInstance : widgetInstances) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.note_widget_layout);
            NoteRepository noteRepository = new NoteRepository(context);
            List<Note> availableNotes = noteRepository.getNotes();
            Note displayNote = NoteUtils.getNoteFromList(availableNotes, SharedPreferencesManager.getDisplayNoteName(context));

            if (displayNote != null) {
                remoteViews.setTextViewText(R.id.note_widget_title, displayNote.getName());
                remoteViews.setTextViewText(R.id.note_widget_text, noteRepository.getNoteContents(noteRepository
                        .getNoteFile(displayNote)));

            } else if (availableNotes != null && availableNotes.size() > 0) {
                remoteViews.setTextViewText(R.id.note_widget_title, availableNotes.get(0).getName());
                remoteViews.setTextViewText(R.id.note_widget_text, noteRepository.getNoteContents(noteRepository
                        .getNoteFile(availableNotes.get(0))));
            } else {
                remoteViews.setTextViewText(R.id.note_widget_title, "");
                remoteViews.setTextViewText(R.id.note_widget_text, "");
            }

            remoteViews.setOnClickPendingIntent(R.id.note_widget_button, getOnClickPendingIntent(context, BUTTON_CLICK_INTENT));
            remoteViews.setOnClickPendingIntent(R.id.note_widget_text, getOnClickPendingIntent(context, TEXT_CLICK_INTENT));
            remoteViews.setOnClickPendingIntent(R.id.note_widget_title, getOnClickPendingIntent(context, TITLE_CLICK_INTENT));

			appWidgetManager.updateAppWidget(widgetInstance, remoteViews);
		}
	}

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        switch (intent.getAction()) {
            case BUTTON_CLICK_INTENT:
                Intent buttonIntent = new Intent(context, SelectionActivity.class);
                buttonIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                buttonIntent.setAction(IntentLibrary.CHOOSE_NOTE_INTENT);
                context.startActivity(buttonIntent);
                break;

            case TEXT_CLICK_INTENT:
                Toast.makeText(context, "Text Clicked", Toast.LENGTH_SHORT).show();
                break;

            case TITLE_CLICK_INTENT:
                Toast.makeText(context, "Title Clicked", Toast.LENGTH_SHORT).show();
                break;

            case IntentLibrary.UPDATE_NOTE_INTENT:
                updateWidgets(context);
                break;
        }
    }

    private PendingIntent getOnClickPendingIntent(Context context, String intentAction) {
        Intent intent = new Intent(context, this.getClass());
        intent.setAction(intentAction);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void updateWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        int[] widgetInstances = getWidgetInstances(context, appWidgetManager);

        if (widgetInstances != null && widgetInstances.length > 0)
            onUpdate(context, appWidgetManager, widgetInstances);
    }

    private int[] getWidgetInstances(Context context, AppWidgetManager appWidgetManager) {
        ComponentName widgetName = new ComponentName(context, this.getClass());
        return appWidgetManager.getAppWidgetIds(widgetName);
    }
}