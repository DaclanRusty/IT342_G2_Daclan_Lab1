package com.daclan.mobile.network

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class AuthUser(
    val email: String,
    val firstName: String,
    val lastName: String
)

data class LoginResponse(
    val token: String,
    val user: AuthUser
)

data class RegisterResponse(
    val message: String,
    val user: AuthUser
)

data class UserResponse(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val createdAt: String
)