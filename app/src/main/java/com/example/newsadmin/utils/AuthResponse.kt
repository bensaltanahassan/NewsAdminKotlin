package com.example.newsadmin.utils

import com.example.newsadmin.models.User

data class LoginResponse (
    val status:Boolean ,
    val message:String ,
    val user:User? = null
)
data class SignUpResponse (
    val status:Boolean ,
    val message:String ,
    val user:User? = null
)
data class ForgetPasswordResponse (
    val status:Boolean ,
    val message:String? = null ,
    val user:User? = null
)
data class SendVerifyCodeResponse (
    val status:Boolean ,
    val message:String? = null ,
)
data class VerifyCodeResponse (
    val status:Boolean ,
    val message:String? = null ,
)

data class ChangePasswordResponse (
    val status:Boolean ,
    val message:String? = null ,
)


