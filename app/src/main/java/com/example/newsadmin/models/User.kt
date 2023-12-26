package com.example.newsadmin.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    val _id: String,
    val firstName: String ,
    val lastName: String,
    val email: String,
    val isAdmin : Boolean,
    val isAccountVerified : Boolean,
    val profilePhoto : Image,
    val __v: Int,
    val createdAt: String,
    val updatedAt: String,
    var token: String? = null
): Parcelable
