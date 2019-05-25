package com.example.erasmusapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.erasmusapp.R
import com.example.erasmusapp.Rest.ApiCon
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import org.eproject.protocol.MemberLocation
import java.util.logging.Logger

class MainJoinGroup : AppCompatActivity() {

    private var btn_doneJoinGroup: Button? = null
    private var edt_InvitationLink: EditText? = null
    private lateinit var lastLocation:Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_join_group)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        btn_doneJoinGroup = findViewById(R.id.btn_doneJoinGroup) as Button
        edt_InvitationLink = findViewById(R.id.edt_InvitationLink) as EditText
        var fotoToByteArray = MainActivity.fotoToByteArray

        val userName = intent.getStringExtra("userName")


        btn_doneJoinGroup!!.setOnClickListener {
            if (edt_InvitationLink!!.text.toString() == "" || edt_InvitationLink!!.text.toString().length < 0) {

                edt_InvitationLink!!.setError(getString(R.string.validation_Insert_Link))

            } else {
                val invKey = edt_InvitationLink!!.text.toString()
                val intent = Intent(this, MapsActivity::class.java)

                intent.putExtra("invKey", invKey)
                intent.putExtra("userName", userName)
                startActivity(intent)
                }
            }
        }
}

