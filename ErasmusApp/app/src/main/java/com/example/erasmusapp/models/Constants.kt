package com.example.erasmusapp.models

import android.app.Application

open class Constants: Application(){

    companion object {
        const val BASE_URL = "http://192.168.1.88/api/"
        const val SUCCESS = "success"
        const val ERRR = "error"
        const val OP_JOIN_GROUP = "OP_JOIN_GROUP"
        const val OP_CREATED = "OP_CREATED"
        const val name_extras = "name_extras"
    }
}