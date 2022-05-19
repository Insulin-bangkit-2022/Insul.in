package com.insulin.app.ui.afiliation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.insulin.app.data.model.AffiliationProduct
import com.insulin.app.databinding.ActivityRecomendationProductBinding
import com.insulin.app.utils.Helper

class RecommendationProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendationProductBinding
    private val listAffiliationProduct: ArrayList<AffiliationProduct> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecomendationProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.loadAffiliationProductData(
            context = this,
            rv = binding.rvAffiliation,
            affiliationProductList = listAffiliationProduct,
            reference = Firebase.database.reference.child("affiliation_product"),
            reversed = true,
            progressBar = binding.progressBarAffiliation,
            horizontalMode = false
        )

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}