package com.insulin.app.ui.afiliation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.insulin.app.adapter.article.AffiliationProductAdapterHorizontal
import com.insulin.app.adapter.article.AffiliationProductAdapterVertical
import com.insulin.app.data.model.AffiliationProduct
import com.insulin.app.databinding.ActivityRecomendationProductBinding
import com.insulin.app.utils.Helper

class RecommendationProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendationProductBinding
    private val list0: ArrayList<AffiliationProduct> = ArrayList() // alat test
    private val list1: ArrayList<AffiliationProduct> = ArrayList() // groceries
    private val list2: ArrayList<AffiliationProduct> = ArrayList() // snack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecomendationProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadAffiliationProductData()

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun loadAffiliationProductData(
    ) {
        binding.progressBarAffiliation.isVisible = true
        val TAG = "FIREBASE"
        Firebase.database.reference.child("affiliation_product")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    list0.clear()
                    list1.clear()
                    list2.clear()
                    for (article in dataSnapshot.children) {
                        val data = article.getValue<AffiliationProduct>()
                        data?.let {
                            when (it.product_category) {
                                0 -> list0.add(it)
                                1 -> list1.add(it)
                                2 -> list2.add(it)
                                else -> {}
                            }
                        }
                    }
                    binding.rvAffiliation0.let {
                        it.setHasFixedSize(true)
                        it.layoutManager = LinearLayoutManager(
                            this@RecommendationProductActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        it.isNestedScrollingEnabled = false
                        list0.reverse()
                        it.adapter = AffiliationProductAdapterHorizontal(list0)
                    }
                    binding.rvAffiliation1.let {
                        it.setHasFixedSize(true)
                        it.layoutManager = LinearLayoutManager(this@RecommendationProductActivity)
                        it.isNestedScrollingEnabled = false
                        list1.reverse()
                        it.adapter = AffiliationProductAdapterVertical(list1)
                    }
                    binding.rvAffiliation2.let {
                        it.setHasFixedSize(true)
                        it.layoutManager = LinearLayoutManager(this@RecommendationProductActivity)
                        it.isNestedScrollingEnabled = false
                        list2.reverse()
                        it.adapter = AffiliationProductAdapterVertical(list2)
                    }
                    binding.progressBarAffiliation.isVisible = false
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    // ...
                }
            })
    }
}