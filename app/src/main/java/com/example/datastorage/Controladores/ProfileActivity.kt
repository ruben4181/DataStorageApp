package com.example.datastorage.Controladores

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.datastorage.Modelos.User
import com.example.datastorage.R
import com.example.datastorage.Servicios.UserServices
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var userServices : UserServices
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    private lateinit var userL : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val bundle = this.intent.extras
        userServices = UserServices(this)
        val user = userServices.getUser(bundle.getString("email"))

        userL=User(user?.idUser, user?.name, user?.email, user?.age, user?.password, user?.img)

        nameProfile.text=user?.name
        ageProfile.text=user?.age.toString()
        emailProfile.text=user?.email.toString()
        val imgButton = findViewById<ImageButton>(R.id.imageButtonProfile)
        imgButton.setImageURI(Uri.parse(user?.img))
        Toast.makeText(this, "HERE="+user?.img, Toast.LENGTH_SHORT).show()
    }

    fun changeProfilePicture(view : View){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
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
        var imgPath =""
        if(resultCode== Activity.RESULT_OK && requestCode==IMAGE_PICK_CODE){
            val fillPath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = this.contentResolver.query(data?.data, fillPath, null, null, null)
            lateinit var path : String
            assert(cursor!=null)
            cursor.moveToFirst()
            imgPath=cursor.getString(cursor.getColumnIndex(fillPath[0]))
            cursor.close()
        }
        val updateUser = User(userL?.idUser, userL?.name, userL?.email, userL?.age, userL?.password, imgPath)
        userServices.updateUser(updateUser)
        imageButtonProfile.setImageURI(Uri.parse(updateUser.img))
    }
}
