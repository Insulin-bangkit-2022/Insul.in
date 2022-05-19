package com.insulin.app.ui.home.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.insulin.app.adapter.article.ArticleAdapter
import com.insulin.app.data.model.Article
import com.insulin.app.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

    private val listArticle: ArrayList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        loadArticleData()
        return binding.root
    }

    private fun loadArticleData() {
        val TAG = "FIREBASE"
        val database = Firebase.database.reference.child("article")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (article in dataSnapshot.children) {
                    val data = article.getValue<Article>()

                    data?.let {
                        listArticle.add(data)
                    }
                }
                binding.rvArticle.let {
                    it.setHasFixedSize(true)
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.isNestedScrollingEnabled = false
                    it.adapter = ArticleAdapter(listArticle)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }

        })
    }

}