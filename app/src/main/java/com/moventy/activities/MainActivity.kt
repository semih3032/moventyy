package com.moventy.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.moventy.R

class MainActivity : AppCompatActivity() {

    private lateinit var selectVideoButton: Button

    private val pickVideoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val intent = Intent(this, EditorActivity::class.java)
            intent.putExtra("videoUri", it.toString())
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectVideoButton = findViewById(R.id.btn_select_video)

        selectVideoButton.setOnClickListener {
            pickVideoLauncher.launch("video/*")
        }
    }
}
