package com.gizmin.bitstore.form_product.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gizmin.bitstore.R

class LoadingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LoadingHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

}

class LoadingHolder(view: View) : RecyclerView.ViewHolder(view)