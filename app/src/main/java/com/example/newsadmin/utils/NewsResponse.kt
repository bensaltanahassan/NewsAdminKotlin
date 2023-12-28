package com.example.newsadmin.utils

import com.example.newsadmin.models.News
import com.example.newsadmin.models.Rating

data class ResponseNewsData(
    val status: String,
    val data: List<News>
)


data class GetSingleNewsResponse(
    val status: String,
    val data: SingleNewsResponse
)


data class  SingleNewsResponse(
    val article: News,
    val rating: Rating? = null,
    val avgRating: Int? = null,
)

data class AddArticleResponse(
    val status: String,
    val data: News
)
data class UpdateArticleResponse(
    val status: String,
    val message: String?= null
)