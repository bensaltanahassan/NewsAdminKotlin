package com.example.newsadmin.models
data class User(
    val _id: String,
    val firstName: String ,
    val lastName: String,
    val email: String,
    val password : String ,
    val isAdmin : Boolean,
    val isAccountVerified : Boolean,
    val profilePhoto : Image,
    val __v: Int,
    val createdAt: String,
    val updatedAt: String,
    var token: String? = null
)
