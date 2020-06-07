package dev.procrastineyaz.watchlist.ui.splash

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.services.TokenService
import dev.procrastineyaz.watchlist.ui.login.LoginActivity
import dev.procrastineyaz.watchlist.ui.main.MainActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private val tokenService: TokenService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#3700B3")
        }
        setContentView(R.layout.activity_splash)
        val nextActivity: Class<out Activity> =
            tokenService.token?.let { MainActivity::class.java } ?: LoginActivity::class.java
        startActivity(Intent(this, nextActivity).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        })

        finish()
    }
}
