package com.example.erasmusapp.Activity

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.*
import com.example.erasmusapp.R
import com.example.erasmusapp.Rest.ApiCon
import org.eproject.protocol.core.Opcode
import java.util.logging.Logger
import android.app.Activity
import android.graphics.Bitmap
import android.provider.MediaStore
import android.net.Uri
import android.graphics.BitmapFactory
import java.io.File
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private var btn_join_group:Button?= null
    private var btn_create_group:Button? = null
    private var selected_picture:ImageButton? = null
    private var edt_name: EditText? = null
    private var selectedImagePath: String? = null


    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val SELECT_PICTURE = 1
        var fotoToByteArray: ByteArray? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initServer()
        initItems()
        getPermission()
    }

    private fun initServer() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val apiCon = ApiCon()

        val Log = Logger.getLogger(MainActivity::class.java.name)
        val opCodeWelcome = apiCon.handShaking()
        if (opCodeWelcome == Opcode.OP_WELCOME){
            Toast.makeText(this, "Welcome to Erasmus Project!", Toast.LENGTH_SHORT).show()
            Log.warning("opCodeWelcome $opCodeWelcome")
        }
    }

    private fun initItems() {
        btn_join_group =  findViewById(R.id.btnJoinGroup) as Button
        btn_create_group = findViewById(R.id.btnCreateGroup) as Button
        selected_picture = findViewById(R.id.fotoButton) as ImageButton
        edt_name = findViewById(R.id.edtName) as EditText

        selected_picture!!.setOnClickListener{ pickImage() }

        btn_join_group!!.setOnClickListener{

            if(edt_name!!.text.toString() == "" || edt_name!!.text.toString().length < 0){
                edt_name!!.setError(getString(R.string.validation_Insert_Name))

            } else {
                val userName = edt_name!!.text.toString()
                val intent = Intent(this, MainJoinGroup::class.java)
                intent.putExtra("userName", userName)
                startActivity(intent)
            }
        }

        btn_create_group!!.setOnClickListener{
            val intent = Intent(this, MainCreateGroup::class.java)
            startActivity(intent)
        }
    }

    fun getPermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MapsActivity.LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MapsActivity.READ_EXTERNAL_STORAGE
            )
            return
        }
    }

    private fun pickImage() {
          val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(
                        intent,
                        "Select Picture"
                    ), SELECT_PICTURE
                )
      }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data!!.data
                selectedImagePath = getPath(selectedImageUri)
                val image = convertBitmap(selectedImagePath!!)
                fotoToByteArray = convertBitmapForByteArray(image)
            }
        }
    }

    private fun convertBitmapForByteArray(image: Bitmap): ByteArray{
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun convertBitmap(urlImage: String): Bitmap{
        val image = File(urlImage)
        val bmOptions = BitmapFactory.Options()
        var bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions)
        selected_picture!!.setImageBitmap(bitmap)
        return bitmap
    }

    fun getPath(uri: Uri?): String? {
        if (uri == null) {
            return null
        }

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(uri, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(column_index)
            cursor.close()
            return path
        }
        return uri!!.getPath()
    }
}
