package com.example.newsadmin.utils

import com.example.newsadmin.models.Favoris
data class ResponseDeleteFavoris(
    val status: String,
    val data: String
)

data class ResponseAddFavoris(
    val status: String,
    val message:String ="",
    val data: Favoris? = null
)


data class ResponseGetFavoris(
    val status: String,
    val data: List<Favoris>
)
