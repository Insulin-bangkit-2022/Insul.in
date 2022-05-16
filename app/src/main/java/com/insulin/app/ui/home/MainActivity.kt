package com.insulin.app.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.location.DetectedActivity
import com.insulin.app.R
import com.insulin.app.databinding.ActivityMainBinding
import com.insulin.app.ui.home.fragment.ArticleFragment
import com.insulin.app.ui.home.fragment.HistoryFragment
import com.insulin.app.ui.home.fragment.HomeFragment
import com.insulin.app.ui.home.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val fragmentHome = HomeFragment()
        val fragmentArticle = ArticleFragment()
        val fragmentHistory = HistoryFragment()
        val fragmentProfile = ProfileFragment()

        activityMainBinding.bottomNavigationView.background = null // hide abnormal layer in bottom nav

        activityMainBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    switchFragment(fragmentHome)
                    true
                }
                R.id.navigation_article -> {
                    switchFragment(fragmentArticle)
                    true
                }
                R.id.navigation_history -> {
                    switchFragment(fragmentHistory)
                    true
                }
                R.id.navigation_profile -> {
                    switchFragment(fragmentProfile)
                    true
                }
                else -> false
            }
        }

        activityMainBinding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, DetectedActivity::class.java)
            startActivity(intent)
        }

    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}