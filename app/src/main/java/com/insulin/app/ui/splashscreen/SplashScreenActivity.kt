package com.insulin.app.ui.splashscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.insulin.app.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        supportActionBar?.hide()

    }
}