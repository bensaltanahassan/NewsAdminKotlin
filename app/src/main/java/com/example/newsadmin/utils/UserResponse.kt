package com.example.newsadmin.utils

import com.example.newsadmin.models.User





data class GetAllUsersResponse(
    val status: String = "",
    val message: String = "",
    val users: List<User>
)

data class UploadResponse(
    val status: Boolean = false,
    val message: String = ""
)

data class UpdateUserResponse(
    val status:Boolean,
    val message: String,
    val user: User? = null,
)