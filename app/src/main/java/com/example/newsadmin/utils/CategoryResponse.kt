package com.example.newsadmin.utils

import com.example.newsadmin.models.Category


data class GetAllCategoriesResponse(
    val status: String,
    val data: List<Category>
)


data class DeleteCategoryResponse(
    val status: String,
    val message: String
)
data class AddCategoryResponse(
    val status: String,
    val message: String?=null,
    val data: Category?=null
)
data class GetSingleCategoryResponse(
    val status: String,
    val data: CategoryData
)
data class CategoryData(
    val category: Category,
    val numberOfArticles: Int
)