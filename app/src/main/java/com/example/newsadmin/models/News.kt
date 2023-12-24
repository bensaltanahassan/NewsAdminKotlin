package com.example.newsadmin.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    val _id:String,
    val title: String,
    val author: String,
    val content: String,
    val categoryId: Category,
    val image: Image,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    var isFavorite:Boolean = false,
): Parcelable
