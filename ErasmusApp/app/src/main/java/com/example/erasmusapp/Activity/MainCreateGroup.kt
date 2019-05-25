package com.example.erasmusapp.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.erasmusapp.R
import com.example.erasmusapp.Rest.ApiCon
import com.example.erasmusapp.Rest.ApiServiceInterface
import com.example.erasmusapp.Rest.ServerRequest
import com.example.erasmusapp.Rest.ServerResponse
import com.example.erasmusapp.models.Constants
import com.example.erasmusapp.models.Users
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Logger

class MainCreateGroup : AppCompatActivity() {

    private var btn_CreateGroup: Button? = null
    private var edt_groupName: EditText? = null
    var GROUP_KEY: String = "Default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_create_group)
        btn_CreateGroup =  findViewById(R.id.btn_CreateGroup) as Button
        edt_groupName = findViewById(R.id.edt_groupName) as EditText


        btn_CreateGroup!!.setOnClickListener{
            if(edt_groupName!!.text.toString() == "" || edt_groupName!!.text.toString().length < 0){
                edt_groupName!!.setError(getString(R.string.edt_CreateGroupName))
            } else {

                val apiCon = ApiCon()
                val Log = Logger.getLogger(MainCreateGroup::class.java.name)
                GROUP_KEY = edt_groupName!!.text.toString()

                var groupKey = apiCon.createGroup(GROUP_KEY)
                val intent = Intent(this, MainGroupKey::class.java)
                intent.putExtra("GROUP_KEY", groupKey)
                startActivity(intent)
                //hmm try now To run app and join group ? create then join reproduce bug
            }
        }//where is the create group ctr lf
    }
}
