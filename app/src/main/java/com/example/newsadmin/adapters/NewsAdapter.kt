package com.example.newsadmin.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsadmin.fragments.HomeFragmentDirections
import com.example.newsadmin.R
import com.example.newsadmin.models.News
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NewsAdapter (
    private val newsList: ArrayList<News>,
    private val navController: NavController,

): RecyclerView.Adapter<NewsAdapter.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_new,parent,false);
        val holder = MyViewHolder(itemView)
        return holder;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.image.visibility = View.GONE
        holder.progressBar.visibility = View.VISIBLE
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


        holder.title.text= currentItem.title
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date:Date = inputFormat.parse(currentItem.createdAt)!!
        val formattedDate:String = outputFormat.format(date)
        holder.date.text = formattedDate
        holder.author.text = currentItem.author

        val currentNew = holder.currentNew
        currentNew.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToArticleDetailsFragment(newsList[position])
            navController.navigate(action)
        }


    }






    override fun getItemCount(): Int {
        return newsList.size;
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val progressBar :ProgressBar = itemView.findViewById(R.id.progressBarNew)
        val currentNew : LinearLayout = itemView.findViewById(R.id.newId)
        val image : ImageView  = currentNew.findViewById(R.id.imageViewNew)
        val title : TextView  = currentNew.findViewById(R.id.titleTextViewNew)
        val date : TextView = currentNew.findViewById(R.id.timeTextViewNew)
        val author: TextView = currentNew.findViewById(R.id.authorTextViewNew)
    }


}