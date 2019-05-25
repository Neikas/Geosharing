package com.example.erasmusapp.Rest

import com.example.erasmusapp.models.Users

class ServerRequest {

    private var operation: String? = null
    private var users: Users? = null

    fun setOperation(operation: String) {
        this.operation = operation
    }

    fun setUsers(users: Users) {
        this.users = users
    }
}
