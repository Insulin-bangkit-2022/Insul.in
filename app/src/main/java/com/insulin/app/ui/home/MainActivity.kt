package com.insulin.app.ui.home

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.insulin.app.R
import com.insulin.app.databinding.ActivityMainBinding
import com.insulin.app.databinding.CustomDialogInfoBinding
import com.insulin.app.ui.afiliation.RecommendationProductActivity
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.ui.home.fragment.ArticleFragment
import com.insulin.app.ui.home.fragment.HistoryFragment
import com.insulin.app.ui.home.fragment.HomeFragment
import com.insulin.app.ui.home.fragment.ProfileFragment
import com.insulin.app.ui.login.LoginActivity
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* init layout */
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        /* check if user not auth -> redirect to login first */
        checkUserAuth()

        /* hide abnormal layer in bottom nav */
        activityMainBinding.bottomNavigationView.background = null

        /* set action for bottom nav */
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

        /* button detection clicked */
        activityMainBinding.fab.setOnClickListener { redirectToDetectDiabetes() }

        /* init firebase remote config for force user to update app while there updates*/
        initRemoteConfig()

        switchFragment(fragmentHome)

    }

    /* init remote config to check updates -> force user to update app if there updates */
    private fun initRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            /*
            *
            *  IMPORTANT -> update this value during deployment time -> change to default minimum time -> 12 hours
            * 10 -> 10 seconds
            * */
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        checkAppUpdates()
    }

    fun redirectToDetectDiabetes() =
        startActivity(Intent(this@MainActivity, DetectionActivity::class.java))

    fun redirectToRecommendationProduct() =
        startActivity(Intent(this@MainActivity, RecommendationProductActivity::class.java))

    fun selectMenu(itemId: Int) {
        activityMainBinding.bottomNavigationView.selectedItemId = itemId
    }

    /* signin out current user */
    fun signOut() {
        Firebase.auth.signOut()
        checkUserAuth()
    }

    /* check user has login / not */
    private fun checkUserAuth() {
        if (Firebase.auth.currentUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            this@MainActivity.finish()
        }
    }

    /* check version updates from firebase server */
    private fun checkAppUpdates() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                val tag = "FirebaseRemoteConfig"
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(tag, "Fetch and activate succeeded, task executed : $updated")
                } else {
                    Log.d(tag, "Fetch Failed")
                }
                val serverAppVersion = remoteConfig["playstore_version"].asString()
                val currentAppVersion = packageManager.getPackageInfo(packageName, 0).versionName
                val isNeededUpdate = Helper.versionCompare(serverAppVersion, currentAppVersion) > 0
                Log.i(
                    tag,
                    "App Version -> $currentAppVersion | Server App Version -> ${serverAppVersion} | Need Updates -> $isNeededUpdate"
                )
                if (isNeededUpdate) {
                    val dialog = Dialog(this@MainActivity)
                    dialog.setCancelable(false)
                    dialog.window!!.apply {
                        attributes.windowAnimations = android.R.transition.fade
                        setGravity(Gravity.CENTER)
                        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    val binding = CustomDialogInfoBinding.inflate(layoutInflater)
                    dialog.setContentView(binding.root)
                    binding.dialogTitle.text = "Update Aplikasi"
                    binding.dialogTitle.gravity = Gravity.CENTER_HORIZONTAL
                    binding.dialogBody.text =
                        "Versi ${serverAppVersion} tersedia. \n\nUntuk bisa tetap menggunakan aplikasi Insul.in, Anda perlu melakukan update aplikasi terlebih dahulu"
                    binding.dialogBody.gravity = Gravity.CENTER_HORIZONTAL
                    binding.btnOk.setOnClickListener {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$packageName")
                                )
                            )
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                )
                            )
                        }
                    }
                    dialog.show()
                }
            }
    }

    /* switch current fragments into new fragments */
    private fun switchFragment(fragment: Fragment) = supportFragmentManager
        .beginTransaction()
        .replace(activityMainBinding.container.id, fragment)
        .commit()

    /* request a permission */
    fun requestPermission(permissions: Array<String>, permissionCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }

    /* do action while user persmission asked */
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
        /* init main activity fragments */
        val fragmentHome = HomeFragment()
        val fragmentArticle = ArticleFragment()
        val fragmentHistory = HistoryFragment()
        val fragmentProfile = ProfileFragment()
    }
}