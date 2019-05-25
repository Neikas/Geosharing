package com.example.erasmusapp.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.erasmusapp.R

class MainGroupKey : AppCompatActivity() {
    private var btn_DONE: Button? = null
    private var edtGroupKey: EditText? = null
    var groupKey: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_group_key)
        btn_DONE =  findViewById(R.id.btn_DONE) as Button
        edtGroupKey = findViewById(R.id.edtGroupKey) as EditText

        groupKey = intent.getStringExtra("GROUP_KEY")

        edtGroupKey!!.setText(groupKey)



        btn_DONE!!.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
