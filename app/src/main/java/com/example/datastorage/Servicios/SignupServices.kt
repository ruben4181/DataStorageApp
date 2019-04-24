package com.example.datastorage.Servicios

import android.content.Context
import com.example.datastorage.Modelos.User

class SignupServices(context : Context){
    private lateinit var user : User
    private var dbConnection : UserDBServices = UserDBServices(context)
    private var sharedConnection : UserReservedServices = UserReservedServices(context)

    fun verifyUser(user: User) : Boolean
    {
        this.user = user
        var result : Boolean = false

        if(sharedConnection.verifyUser(user))
        {
            result =  sharedConnection.verifyUser(this.user)
        }
        else
            result =  dbConnection.verifyUser(this.user)
        return result
    }

    fun saveUser(user : User) : Boolean
    {
        this.user=user
        dbConnection.saveUser(this.user)
        return verifyUser(user)
    }
}