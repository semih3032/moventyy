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

class VideoExportActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var btnExport: Button
    private lateinit var resolutionSpinner: Spinner
    private lateinit var selectedVideoUri: Uri
    private var videoSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_export)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnExport = findViewById(R.id.btnExport)
        resolutionSpinner = findViewById(R.id.resolutionSpinner)

        val resolutionOptions = arrayOf("720p", "1080p", "4K")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, resolutionOptions)
        resolutionSpinner.adapter = adapter

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 11001)
        }

        btnExport.setOnClickListener {
            if (!videoSelected) {
                Toast.makeText(this, "Lütfen önce bir video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedResolution = resolutionSpinner.selectedItem.toString()
            val output = File(cacheDir, "exported_video_${selectedResolution}.mp4")

            val resolutionCommand = when (selectedResolution) {
                "720p" -> "-s 1280x720"
                "1080p" -> "-s 1920x1080"
                "4K" -> "-s 3840x2160"
                else -> ""
            }

            val command = "-i ${getPathFromUri(selectedVideoUri)} $resolutionCommand ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Video başarıyla dışa aktarıldı!", Toast.LENGTH_SHORT).show()
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
