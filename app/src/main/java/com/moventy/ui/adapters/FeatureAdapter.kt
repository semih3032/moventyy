package com.moventy.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moventy.R
import com.moventy.ui.models.FeatureItem

class FeatureAdapter(private val features: List<FeatureItem>) :
    RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {

    inner class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val featureTitle: TextView = itemView.findViewById(R.id.featureTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feature, parent, false)
        return FeatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        val feature = features[position]
        holder.featureTitle.text = feature.title
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, feature.activityClass)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = features.size
}
