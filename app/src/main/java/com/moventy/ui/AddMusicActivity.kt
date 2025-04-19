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

class AddMusicActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var btnPickMusic: Button
    private lateinit var btnAddMusic: Button
    private lateinit var selectedVideoUri: Uri
    private lateinit var selectedMusicUri: Uri
    private var videoSelected = false
    private var musicSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_music)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnPickMusic = findViewById(R.id.btnPickMusic)
        btnAddMusic = findViewById(R.id.btnAddMusic)

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 4001)
        }

        btnPickMusic.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            startActivityForResult(intent, 4002)
        }

        btnAddMusic.setOnClickListener {
            if (!videoSelected || !musicSelected) {
                Toast.makeText(this, "Lütfen hem video hem müzik seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "music_added_output.mp4")
            val command = "-i ${getPathFromUri(selectedVideoUri)} -i ${getPathFromUri(selectedMusicUri)} -map 0:v -map 1:a -c:v copy -shortest ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Müzik başarıyla eklendi!", Toast.LENGTH_SHORT).show()
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
            when (requestCode) {
                4001 -> {
                    selectedVideoUri = data.data!!
                    videoSelected = true
                    Toast.makeText(this, "Video seçildi", Toast.LENGTH_SHORT).show()
                }
                4002 -> {
                    selectedMusicUri = data.data!!
                    musicSelected = true
                    Toast.makeText(this, "Müzik seçildi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndex("_data")
            cursor.getString(index) ?: uri.path ?: ""
        } else {
            uri.path ?: ""
        }
    }
}