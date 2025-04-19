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

class VideoFilterActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var btnApplyFilter: Button
    private lateinit var filterSpinner: Spinner
    private lateinit var selectedVideoUri: Uri
    private var videoSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_filter)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnApplyFilter = findViewById(R.id.btnApplyFilter)
        filterSpinner = findViewById(R.id.filterSpinner)

        val filterOptions = arrayOf("Sepia", "Grayscale", "Invert", "Brightness", "Contrast")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterOptions)
        filterSpinner.adapter = adapter

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 8001)
        }

        btnApplyFilter.setOnClickListener {
            if (!videoSelected) {
                Toast.makeText(this, "Lütfen önce bir video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedFilter = filterSpinner.selectedItem.toString()
            val output = File(cacheDir, "filtered_video_output.mp4")
            val filterCommand = when (selectedFilter) {
                "Sepia" -> "-vf sepia ${output.absolutePath}"
                "Grayscale" -> "-vf hue=s=0 ${output.absolutePath}"
                "Invert" -> "-vf lutrgb=r=negval:g=negval:b=negval ${output.absolutePath}"
                "Brightness" -> "-vf eq=brightness=0.06 ${output.absolutePath}"
                "Contrast" -> "-vf eq=contrast=1.5 ${output.absolutePath}"
                else -> ""
            }

            val command = "-i ${getPathFromUri(selectedVideoUri)} $filterCommand"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Filtre başarıyla uygulandı!", Toast.LENGTH_SHORT).show()
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
