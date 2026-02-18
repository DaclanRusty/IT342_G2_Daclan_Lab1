package com.daclan.mobile.network

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("/api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("/api/user/me")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserResponse>
}