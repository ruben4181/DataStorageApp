package com.example.datastorage.Controladores

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.datastorage.Modelos.User
import com.example.datastorage.R
import com.example.datastorage.Servicios.SignupServices

class RegisterActivity : AppCompatActivity() {
    private lateinit var signupServices: SignupServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        signupServices = SignupServices(this)
    }

    fun signUp(view: View)
    {
        val nombre = this.findViewById<EditText>(R.id.nombre_reg)
        val correo = this.findViewById<EditText>(R.id.correo_reg)
        val password = this.findViewById<EditText>(R.id.password_reg)
        val edad = this.findViewById<EditText>(R.id.edad_reg)

        val user = User(null, nombre.text.toString(), correo.text.toString(),
            edad.text.toString().toInt(), password.text.toString())
        if(!signupServices.verifyUser(user)){
            if(signupServices.saveUser(user)){
                Toast.makeText(this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "El usuario no pudo ser registrado", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
        }
    }
}