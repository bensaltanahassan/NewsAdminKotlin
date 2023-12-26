package com.example.newsadmin.utils

import com.example.newsadmin.models.Category


data class GetAllCategoriesResponse(
    val status: String,
    val data: List<Category>
)