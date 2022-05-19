package com.insulin.app.adapter.article

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.insulin.app.data.model.AffiliationProduct
import com.insulin.app.databinding.ItemAffiliationWideBinding
import com.insulin.app.ui.webview.WebViewActivity
import com.insulin.app.utils.Helper

class AffiliationProductAdapterVertical(private val data: ArrayList<AffiliationProduct>) :
    RecyclerView.Adapter<AffiliationProductAdapterVertical.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAffiliationWideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(private val binding: ItemAffiliationWideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: AffiliationProduct) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.product_image_url)
                    .into(productImage)
                Glide.with(itemView.context)
                    .load(product.product_store_image_url)
                    .into(productProvider)
                productName.text = product.product_name
                productPrice.text = Helper.getRupiahFormat(product.product_price)
                itemView.setOnClickListener {
                    Helper.openLinkInBrowser(itemView.context, product.product_url)
                }
            }

        }
    }

}