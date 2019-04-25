package com.example.datastorage.Controladores

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.datastorage.Modelos.User
import com.example.datastorage.R
import com.example.datastorage.Servicios.SignupServices
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var signupServices: SignupServices
    private var imgPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        signupServices = SignupServices(this)
    }

    fun pickProfilePicture(view : View){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }else{
                pickImageFromGallery()
            }
        }else{
            pickImageFromGallery()
        }
    }

    fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }else{
                    Toast.makeText(this, "Permiso de acceso a galeria denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode : Int, data : Intent?) {
        if(resultCode==Activity.RESULT_OK && requestCode== IMAGE_PICK_CODE){
            val fillPath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = this.contentResolver.query(data?.data, fillPath, null, null, null)
            lateinit var path : String
            assert(cursor!=null)
            cursor.moveToFirst()
            imgPath=cursor.getString(cursor.getColumnIndex(fillPath[0]))
            cursor.close()
        }
    }

    fun signUp(view: View)
    {
        val nombre = this.findViewById<EditText>(R.id.nombre_reg)
        val correo = this.findViewById<EditText>(R.id.correo_reg)
        val password = this.findViewById<EditText>(R.id.password_reg)
        val edad = this.findViewById<EditText>(R.id.edad_reg)

        val user = User(null, nombre.text.toString(), correo.text.toString(),
            edad.text.toString().toInt(), password.text.toString(), imgPath)
        if(!signupServices.verifyUser(user)){
            if(signupServices.saveUser(user)){
                var user2 = signupServices.getUser(correo.text.toString())
                imageButton.setImageURI(Uri.parse(user2?.img))
                Toast.makeText(this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "El usuario no pudo ser registrado", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }
}