package com.example.newsadmin.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(val url : String , val publicId : String? ): Parcelable
