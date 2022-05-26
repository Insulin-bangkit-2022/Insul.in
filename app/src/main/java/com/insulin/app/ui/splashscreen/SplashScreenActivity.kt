package com.insulin.app.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insulin.app.databinding.ActivitySplashScreenBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.onBoarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* disable dark mode*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /* init layout */
        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        /* force full screen for splash screen */
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val auth = Firebase.auth
        Handler().postDelayed({
            if (auth.currentUser != null) {
                /* user was login */
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreenActivity, OnBoardingActivity::class.java))
            }
            finish()
        }, 2000)

    }
}