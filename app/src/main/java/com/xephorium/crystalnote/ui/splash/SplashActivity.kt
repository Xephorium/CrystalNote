package com.xephorium.crystalnote.ui.splash

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.os.Handler
import com.xephorium.crystalnote.data.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerButton.*
import com.xephorium.crystalnote.ui.home.HomeActivity

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Initial Drawer Button Selection
        val sharedPreferencesRepository = SharedPreferencesRepository(this)
        sharedPreferencesRepository.setSelectedDrawerButton(NOTES)

        Handler().postDelayed(Runnable {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }
}