package com.example.newsadmin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsadmin.R
import com.example.newsadmin.fragments.CategoriesFragmentDirections
import com.example.newsadmin.models.Category
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CategoriesAdapter(
    private val categoryList: ArrayList<Category>,
    private val navController: NavController,

    ): RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_category,parent,false);
        val holder = MyViewHolder(itemView)
        return holder;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem:Category = categoryList[position]
        holder.title.text= currentItem.name
        //load the images
        val request = coil.request.ImageRequest.Builder(holder.image.context)
            .data(currentItem.image.url)
            .target(holder.image)
            .target(
                onStart = {
                    holder.progressBar.visibility = View.VISIBLE
                },
                onSuccess = { result ->
                    holder.progressBar.visibility = View.GONE
                    holder.image.visibility = View.VISIBLE
                    holder.image.setImageDrawable(result)
                },
                onError = { _ ->
                    holder.progressBar.visibility = View.GONE
                    holder.image.visibility = View.VISIBLE
                    holder.image.setImageDrawable(ContextCompat.getDrawable(holder.image.context,R.drawable.baseline_error_outline_24))
                }
            ).build()
        coil.ImageLoader(holder.image.context).enqueue(request)


        //date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date: Date = inputFormat.parse(currentItem.createdAt)!!
        val formattedDate:String = outputFormat.format(date)
        holder.date.text = formattedDate


        holder.itemView.setOnClickListener {
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToCategoryDetailsFragment(currentItem)
            navController.navigate(action)

        }
    }








    override fun getItemCount(): Int {
        return categoryList.size;
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image:ImageView = itemView.findViewById(R.id.imageViewCategory)
        val title : TextView = itemView.findViewById(R.id.titleTextViewCategory)
        val date : TextView = itemView.findViewById(R.id.dateTextViewCategory)
        val progressBar : ProgressBar = itemView.findViewById(R.id.progressBarImageCategory)



    }


}