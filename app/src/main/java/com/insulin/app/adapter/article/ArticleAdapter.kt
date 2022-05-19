package com.insulin.app.adapter.article

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.insulin.app.data.model.Article
import com.insulin.app.databinding.ItemArticleListBinding
import com.insulin.app.ui.webview.WebViewActivity
import com.insulin.app.utils.Helper

class ArticleAdapter(private val data: ArrayList<Article>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(private val binding: ItemArticleListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(article.image_url)
                    .into(ivDiagnoseResultIcon)

                tvDiagnoseTime.text = article.provider_name
                tvDiagnoseResult.text = article.title
                itemView.setOnClickListener {
                    Helper.openLinkInWebView(itemView.context,article.url)
                }
            }

        }
    }


}