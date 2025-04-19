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

class VideoCutActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var btnCutVideo: Button
    private lateinit var startTimeInput: EditText
    private lateinit var endTimeInput: EditText
    private lateinit var selectedVideoUri: Uri
    private var videoSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_cut)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnCutVideo = findViewById(R.id.btnCutVideo)
        startTimeInput = findViewById(R.id.inputStart)
        endTimeInput = findViewById(R.id.inputEnd)

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 1001)
        }

        btnCutVideo.setOnClickListener {
            if (!videoSelected) {
                Toast.makeText(this, "Önce bir video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val startTime = startTimeInput.text.toString()
            val endTime = endTimeInput.text.toString()

            if (startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Başlangıç ve bitiş zamanı girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "cut_output.mp4")
            val command = "-i ${getPathFromUri(selectedVideoUri)} -ss $startTime -to $endTime -c copy ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Video başarıyla kesildi!", Toast.LENGTH_SHORT).show()
                    // ExportActivity'ye gönderilebilir
                },
                onFailure = { error ->
                    Toast.makeText(this, "Hata: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                selectedVideoUri = it
                videoSelected = true
                Toast.makeText(this, "Video seçildi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String {
        // Geçici çözüm: URI'dan dosya yolu almak
        return uri.path ?: ""
    }
}
