package com.udacity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val name=intent?.getStringExtra("name").toString()
        val status= intent?.getStringExtra("status")!!.toBoolean()
        val Name=findViewById<TextView>(R.id.textView3)
        val Status=findViewById<TextView>(R.id.textView4)
        val DoneButton=findViewById<Button>(R.id.button)
        Name.text=name
        when(status){
            true->{Status.setTextColor(Color.BLUE)
                 Status.text="Done"}
            false->{
                Status.setTextColor(Color.RED)
                Status.text= "Failed"}
        }
        notificationManager.cancelAll()
        DoneButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }


}
