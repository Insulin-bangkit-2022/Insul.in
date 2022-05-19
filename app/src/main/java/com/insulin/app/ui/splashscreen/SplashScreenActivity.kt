package com.insulin.app.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insulin.app.databinding.ActivitySplashScreenBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.onBoarding.OnBoardingActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* disable dark mode*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val auth = Firebase.auth

        Timer().schedule(2000) {
            if (auth.currentUser != null) {
                /* user was login */
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreenActivity, OnBoardingActivity::class.java))
            }
            finish()
        }
    }
}