package com.moventy.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.moventy.R
import com.moventy.utils.FFmpegCommandRunner
import java.io.File

class VideoSpeedActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var btnChangeSpeed: Button
    private lateinit var speedEditText: EditText
    private lateinit var selectedVideoUri: Uri
    private var videoSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_speed)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnChangeSpeed = findViewById(R.id.btnChangeSpeed)
        speedEditText = findViewById(R.id.editTextSpeed)

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 7001)
        }

        btnChangeSpeed.setOnClickListener {
            if (!videoSelected) {
                Toast.makeText(this, "Lütfen önce bir video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val speed = speedEditText.text.toString()
            if (speed.isEmpty()) {
                Toast.makeText(this, "Lütfen hız değerini girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "speed_changed_video_output.mp4")
            val command = "-i ${getPathFromUri(selectedVideoUri)} -filter:v \"setpts=$speed*PTS\" -an ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Video hızı başarıyla değiştirildi!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(this, "Hata: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedVideoUri = data.data!!
            videoSelected = true
            Toast.makeText(this, "Video seçildi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPathFromUri(uri: Uri): String {
        return uri.path ?: ""
    }
}
