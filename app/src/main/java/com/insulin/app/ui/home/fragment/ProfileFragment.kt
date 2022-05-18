package com.insulin.app.ui.home.fragment

import android.content.Context
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
import com.insulin.app.ui.login.LoginActivity

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
            val context: Context = (activity as LoginActivity)
            Firebase.auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            (activity as LoginActivity).startActivity(intent)

        }

        return binding.root
    }

}