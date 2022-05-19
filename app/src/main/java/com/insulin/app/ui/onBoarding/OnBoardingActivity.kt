package com.insulin.app.ui.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.insulin.app.R
import com.insulin.app.databinding.ActivityOnBoardingBinding
import com.insulin.app.ui.login.LoginActivity
import com.insulin.app.ui.onBoarding.screens.OnBoarding1Fragment
import com.insulin.app.ui.onBoarding.screens.OnBoarding2Fragment
import com.insulin.app.ui.onBoarding.screens.OnBoarding3Fragment

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var onBoardingBinding: ActivityOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBoardingBinding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(onBoardingBinding.root)
        val screen = OnBoarding1Fragment()
        switchScreen(screen)
    }

    fun switchScreen(
        fragment: Fragment,
        image: ImageView? = null,
        title: TextView? = null,
        subtitle: TextView? = null,
        indicator1: ImageView? = null,
        indicator2: ImageView? = null,
        indicator3: ImageView? = null,
        btnPrev: Button? = null,
        btnNext: Button? = null
    ) {
        supportFragmentManager
            .beginTransaction().apply {
                replace(R.id.container, fragment)
                image?.let {
                    addSharedElement(it, "image")
                }
                title?.let {
                    addSharedElement(it, "title")
                }
                subtitle?.let {
                    addSharedElement(it, "subtitle")
                }
                indicator1?.let {
                    addSharedElement(it, "indicator1")
                }
                indicator2?.let {
                    addSharedElement(it, "indicator2")
                }
                indicator3?.let {
                    addSharedElement(it, "indicator3")
                }
                btnPrev?.let {
                    addSharedElement(it, "btnPrev")
                }
                btnNext?.let {
                    addSharedElement(it, "btnNext")
                }
                commit()
            }


    }

    fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    companion object {
        val screen1 = OnBoarding1Fragment()
        val screen2 = OnBoarding2Fragment()
        val screen3 = OnBoarding3Fragment()
    }
}