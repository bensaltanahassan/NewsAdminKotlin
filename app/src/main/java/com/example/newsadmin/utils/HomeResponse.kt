package com.example.newsadmin.utils

import com.example.newsadmin.models.Category
import com.example.newsadmin.models.Favoris
import com.example.newsadmin.models.News

data class ResponseHomeData(
    val status: String,
    val data: DataHome
)

data class DataHome(
    val categories: List<Category>,
    val news: List<News>,
    val favoris: List<Favoris>
)