package com.insulin.app.ui.home.fragment


import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.DetectedActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insulin.app.R
import com.insulin.app.databinding.ActivityMainBinding
import com.insulin.app.databinding.FragmentHomeBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.maps.MapsActivity
import com.insulin.app.ui.webview.WebViewActivity
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper
import java.security.Permission

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val user = Firebase.auth.currentUser
        user?.let {
            binding.userDisplayName.text =
                Helper.getWelcomeGreetings(it.displayName ?: "Pengguna Insul.in")
            Glide.with(this@HomeFragment)
                .load(it.photoUrl)
                .into(binding.userAvatar)
        }

        binding.let {

            /* RS terdekat */
            it.shortuctHospital.setOnClickListener {
                if (Helper.isPermissionGranted(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    val intent =
                        Intent(context, MapsActivity::class.java)
                    (activity as MainActivity).startActivity(intent)
                } else {
                    (activity as MainActivity).requestPermission(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), Constanta.LOCATION_PERMISSION_CODE
                    )
                }
            }

            /* konsultasi dokter */
            it.shortuctConsultation.setOnClickListener {
                val intent =
                    Intent(context, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.EXTRA_WEBVIEW, "https://apriantoa917.github.io")
                (activity as MainActivity).startActivity(intent)
            }

            it.shortuctInfo.setOnClickListener {
                val intent =
                    Intent(context, WebViewActivity::class.java)
                intent.putExtra(
                    WebViewActivity.EXTRA_WEBVIEW,
                    "https://hellosehat.com/diabetes/diabetes-melitus/"
                )
                (activity as MainActivity).startActivity(intent)
            }

            it.btnHelp.setOnClickListener {
                Helper.showDialogDiagnoseResult(requireContext(), isDiabetes = false)
            }
        }

        return binding.root
    }


}