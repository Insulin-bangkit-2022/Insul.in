package com.insulin.app.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insulin.app.R
import com.insulin.app.databinding.ActivityMainBinding
import com.insulin.app.ui.afiliation.RecommendationProductActivity
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.ui.home.fragment.ArticleFragment
import com.insulin.app.ui.home.fragment.HistoryFragment
import com.insulin.app.ui.home.fragment.HomeFragment
import com.insulin.app.ui.home.fragment.ProfileFragment
import com.insulin.app.ui.login.LoginActivity
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val user = Firebase.auth.currentUser
        if (user != null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        /* disable dark mode*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)



        activityMainBinding.bottomNavigationView.background =
            null // hide abnormal layer in bottom nav


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
            val intent = Intent(this@MainActivity, DetectionActivity::class.java)
            startActivity(intent)
        }

        switchFragment(fragmentHome)

    }

    fun selectMenu(itemId: Int) {
        activityMainBinding.bottomNavigationView.selectedItemId = itemId
    }

    fun redirectToRecommendationProduct(){
        startActivity(Intent(this@MainActivity,RecommendationProductActivity::class.java))
    }


    fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    public fun requestPermission(permissions: Array<String>, permissionCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            Constanta.CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(this, "Berikan aplikasi izin mengakses kamera  ")
                }
            }
            Constanta.LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(
                        this,
                        "Berikan aplikasi izin lokasi untuk membaca lokasi  "
                    )
                }
            }
            Constanta.STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(
                        this,
                        "Berikan aplikasi izin storage untuk membaca dan menyimpan story"
                    )
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        val fragmentHome = HomeFragment()
        val fragmentArticle = ArticleFragment()
        val fragmentHistory = HistoryFragment()
        val fragmentProfile = ProfileFragment()
    }
}