package com.xephorium.crystalnote.ui.splash

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.os.Handler
import com.xephorium.crystalnote.ui.home.HomeActivity

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(Runnable {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }
}