package com.example.newsadmin.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rate(
    val rating:Int,
    val user:String,
    val article:String,
):Parcelable
