package com.example.datastorage.Servicios

import android.content.Context
import com.example.datastorage.Modelos.User

class UserServices(context: Context){
    private var dbConnection : UserDBServices = UserDBServices(context)

    fun getUser(email : String) : User?{
        return dbConnection.getUser(email)
    }
    fun updateUser(user : User) : Boolean {
        dbConnection.updateUser(user)
        return true
    }
}