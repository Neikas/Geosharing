package com.example.erasmusapp.Rest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceInterface {

    @POST("Users/userApp")
    fun getUsers(@Body request: ServerRequest): Call<ServerResponse>

    @POST("Users/joinGroup")
    fun joinGroup(@Body request: ServerRequest): Call<ServerResponse>
}