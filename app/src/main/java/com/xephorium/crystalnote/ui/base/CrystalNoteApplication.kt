package com.xephorium.crystalnote.ui.base

import android.app.Application
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository

class CrystalNoteApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Reset Persisted showArchivedNotes State on App Launch
        val sharedPreferencesRepository = SharedPreferencesRepository(this)
        sharedPreferencesRepository.setShowArchivedNotesEnabled(false)
    }
}