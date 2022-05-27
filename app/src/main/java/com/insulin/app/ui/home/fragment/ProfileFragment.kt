package com.insulin.app.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insulin.app.databinding.FragmentProfileBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.reminderNotifications.ReminderNotificationsActivity
import com.insulin.app.ui.webview.WebViewActivity
import com.insulin.app.utils.Constanta

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val user = Firebase.auth.currentUser
        user?.let {
            Glide.with(this)
                .load(user.photoUrl)
                .into(binding.civUserProfile)
            binding.name.text = user.displayName
            binding.email.text = user.email
        }

        binding.btnLogout.setOnClickListener {
            (activity as MainActivity).signOut()
        }

        binding.reminder.setOnClickListener {
            val intent = Intent((activity as MainActivity), ReminderNotificationsActivity::class.java)
            startActivity(intent)
        }

        binding.about.setOnClickListener {
            val intent = Intent((activity as MainActivity), WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.EXTRA_WEBVIEW, Constanta.LINK_WEB_TENTANG_APLIKASI)
            startActivity(intent)
        }

        binding.attribution.setOnClickListener {
            val intent = Intent((activity as MainActivity), WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.EXTRA_WEBVIEW, Constanta.LINK_WEB_ATTRIBUTION)
            startActivity(intent)
        }

        return binding.root
    }

}