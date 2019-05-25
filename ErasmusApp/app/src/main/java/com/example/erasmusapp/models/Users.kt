package com.example.erasmusapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Results(
    @SerializedName("result") val result:String,
    @SerializedName("message") val message:String,
    @SerializedName("groupKey") val groupKey:String
)

data class Users (
    @SerializedName("name") val name:String,
    @SerializedName("latitude")val latitude:String,
    @SerializedName("longitute")val longitute:String,
    @SerializedName("nameGroup")val nameGroup:String
): Serializable
