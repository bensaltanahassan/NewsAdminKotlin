package com.example.newsadmin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Category(
    val _id:String,
    val name:String,
    val description: String,
    val image: Image,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
):Parcelable
