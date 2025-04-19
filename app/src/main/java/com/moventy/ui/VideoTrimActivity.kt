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

class VideoTrimActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var btnTrimVideo: Button
    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var selectedVideoUri: Uri
    private var videoSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_trim)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnTrimVideo = findViewById(R.id.btnTrimVideo)
        startTimeEditText = findViewById(R.id.editTextStartTime)
        endTimeEditText = findViewById(R.id.editTextEndTime)

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 6001)
        }

        btnTrimVideo.setOnClickListener {
            if (!videoSelected) {
                Toast.makeText(this, "Lütfen önce bir video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()

            if (startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Başlangıç ve bitiş zamanlarını girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "trimmed_video_output.mp4")
            val command = "-i ${getPathFromUri(selectedVideoUri)} -ss $startTime -to $endTime -c:v copy -c:a copy ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Video başarıyla kesildi!", Toast.LENGTH_SHORT).show()
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
