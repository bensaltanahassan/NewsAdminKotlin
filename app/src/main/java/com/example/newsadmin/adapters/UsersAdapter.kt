package com.example.newsadmin.adapters

import android.annotation.SuppressLint
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
import com.example.newsadmin.models.User


class UsersAdapter(
    private val usersList: ArrayList<User>,
    private val navController: NavController,

    ): RecyclerView.Adapter<UsersAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_user,parent,false);
        val holder = MyViewHolder(itemView)
        return holder;
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem:User = usersList[position]
        holder.title.text= currentItem.firstName+" "+currentItem.lastName
        holder.email.text= currentItem.email
        //load the images
        val request = coil.request.ImageRequest.Builder(holder.image.context)
            .data(currentItem.profilePhoto.url)
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




        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return usersList.size;
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image:ImageView = itemView.findViewById(R.id.imageViewSingleUser)
        val title : TextView = itemView.findViewById(R.id.titleTextViewSingleUser)
        val email : TextView = itemView.findViewById(R.id.dateTextViewSingleUser)
        val progressBar : ProgressBar = itemView.findViewById(R.id.progressBarImageSingleUser)

    }


}