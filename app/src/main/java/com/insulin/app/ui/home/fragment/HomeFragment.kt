package com.insulin.app.ui.home.fragment


import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.insulin.app.R
import com.insulin.app.data.model.AffiliationProduct
import com.insulin.app.data.model.Article
import com.insulin.app.data.model.Detection
import com.insulin.app.databinding.FragmentHomeBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.maps.MapsActivity
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val listArticle: ArrayList<Article> = ArrayList()
    private val listAffiliationProduct: ArrayList<AffiliationProduct> = ArrayList()
    private val listHistory: ArrayList<Detection> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val context: Context = (activity as MainActivity)
        Firebase.auth.currentUser?.let {
            binding.userDisplayName.text = it.displayName
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
                        context, Manifest.permission.ACCESS_FINE_LOCATION
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

            /* rekomendasi produk */
            it.shortuctAffiliation.setOnClickListener {
                (activity as MainActivity).redirectToRecommendationProduct()
            }

            /* komunitas diabetes */
            it.shortuctCommunity.setOnClickListener {
                (activity as MainActivity).redirectToRecommendationProduct()
            }

            /* konsultasi dokter */
            it.shortuctConsultation.setOnClickListener {
                Helper.openLinkInWebView(context,Constanta.LINK_WEB_KONSULTASI_DOKTER)
            }

            /* cek lab */
            it.shortuctLab.setOnClickListener {
                Helper.openLinkInWebView(context,Constanta.LINK_WEB_CEK_LAB)
            }

            /* monitoring kadar gula darah */
            it.shortuctLab.setOnClickListener {
                Helper.openLinkInWebView(context,Constanta.LINK_WEB_MONITORING_GULA_DARAH)
            }

            /* btn help */
            it.btnHelp.setOnClickListener {
                Helper.openLinkInWebView(context,Constanta.LINK_WEB_HELP)
            }

            /* see more article */
            it.btnMoreArticle.setOnClickListener {
                (activity as MainActivity).selectMenu(R.id.navigation_article)
            }

            /* see more recommendation product */
            it.btnMoreAffiliation.setOnClickListener {
                binding.shortuctAffiliation.performClick()
            }

            /* see more detection history */
            it.btnMoreDetection.setOnClickListener {
                (activity as MainActivity).selectMenu(R.id.navigation_history)
            }
        }
        /* init article list */
        Helper.loadArticleData(
            context = requireContext(),
            rv = binding.rvArticle,
            articleList = listArticle,
            reference = Firebase.database.reference.child("article").limitToLast(3),
            reversed = true,
            progressBar = binding.progressBarArticle
        )

        /* init affiliation product list */
        Helper.loadAffiliationProductData(
            context = requireContext(),
            rv = binding.rvAffiliation,
            affiliationProductList = listAffiliationProduct,
            reference = Firebase.database.reference.child("affiliation_product").limitToLast(3),
            reversed = true,
            progressBar = binding.progressBarAffiliation
        )
        binding.progressBarHistory.isVisible = true
        binding.labelNeverDetecting.isVisible = false
        Helper.loadHistoryData(
            context = requireContext(),
            containerNeverDetecting = binding.labelNeverDetecting,
            btnDetect = binding.btnDetection,
            progressBar = binding.progressBarHistory,
            recyclerView = binding.rvHistory,
            listHistory = listHistory,
            limit = 3,
            btnMoreDetection = binding.btnMoreDetection
        )
        return binding.root
    }



}