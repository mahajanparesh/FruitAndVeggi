package com.example.retriving_data.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retriving_data.R
import com.example.retriving_data.models.Product

open class MyProducts(
        private val context: Context,
        private var list: ArrayList<Product>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position];
        model.image;
        if(holder is MyViewHolder){
        holder.itemView.findViewById<TextView>(R.id.tv_item_name)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view);
}