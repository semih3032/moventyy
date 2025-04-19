package com.moventy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.moventy.R

class MainActivity : AppCompatActivity() {

    private lateinit var btnVideoEditing: Button
    private lateinit var btnAddAudio: Button
    private lateinit var btnVideoCollage: Button
    private lateinit var btnVideoExport: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVideoEditing = findViewById(R.id.btnVideoEditing)
        btnAddAudio = findViewById(R.id.btnAddAudio)
        btnVideoCollage = findViewById(R.id.btnVideoCollage)
        btnVideoExport = findViewById(R.id.btnVideoExport)

        btnVideoEditing.setOnClickListener {
            val intent = Intent(this, VideoEffectActivity::class.java)
            startActivity(intent)
        }

        btnAddAudio.setOnClickListener {
            val intent = Intent(this, AudioTrackActivity::class.java)
            startActivity(intent)
        }

        btnVideoCollage.setOnClickListener {
            val intent = Intent(this, VideoCollageActivity::class.java)
            startActivity(intent)
        }

        btnVideoExport.setOnClickListener {
            val intent = Intent(this, VideoExportActivity::class.java)
            startActivity(intent)
        }
    }
}
