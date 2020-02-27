package com.xephorium.crystalnote.ui.base

import android.annotation.SuppressLint
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Set App Theme
        val sharedPreferencesRepository = SharedPreferencesRepository(this)
        val themeName = sharedPreferencesRepository.getTheme()
        val theme = resources.getIdentifier(themeName, "style", packageName)
        setTheme(theme)

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
