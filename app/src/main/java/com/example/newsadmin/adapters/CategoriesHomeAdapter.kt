package com.example.newsadmin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsadmin.R
import com.example.newsadmin.models.Category


class CategoriesHomeAdapter(
    private val categoryList: ArrayList<Category>,
    private val onClickCategory: (Category) -> Unit,
    private var currentCategory: Category?

): RecyclerView.Adapter<CategoriesHomeAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_category_home,parent,false);
        val holder = MyViewHolder(itemView)
        return holder;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem:Category = categoryList[position]
        holder.title.text= currentItem.name

        holder.itemView.isSelected = (currentItem == currentCategory)
        holder.title.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (holder.itemView.isSelected) R.color.white else R.color.black
            )
        )

        holder.itemView.setOnClickListener {
            if (currentItem != currentCategory) {
                currentCategory = currentItem
                onClickCategory(currentItem)
                notifyDataSetChanged()
            }
        }
    }








    override fun getItemCount(): Int {
        return categoryList.size;
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.custom_category_home)

    }


}