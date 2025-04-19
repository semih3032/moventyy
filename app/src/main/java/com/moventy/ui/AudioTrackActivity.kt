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

class AudioTrackActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var btnPickAudio: Button
    private lateinit var btnAddAudioTrack: Button
    private lateinit var selectedVideoUri: Uri
    private lateinit var selectedAudioUri: Uri
    private var videoSelected = false
    private var audioSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_track)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnPickAudio = findViewById(R.id.btnPickAudio)
        btnAddAudioTrack = findViewById(R.id.btnAddAudioTrack)

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 9001)
        }

        btnPickAudio.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            startActivityForResult(intent, 9002)
        }

        btnAddAudioTrack.setOnClickListener {
            if (!videoSelected) {
                Toast.makeText(this, "Lütfen önce bir video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!audioSelected) {
                Toast.makeText(this, "Lütfen önce bir ses dosyası seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "video_with_audio_output.mp4")
            val command = "-i ${getPathFromUri(selectedVideoUri)} -i ${getPathFromUri(selectedAudioUri)} -c:v copy -c:a aac -strict experimental ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Ses dosyası başarıyla eklendi!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(this, "Hata: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 9001 && data.data != null) {
                selectedVideoUri = data.data!!
                videoSelected = true
                Toast.makeText(this, "Video seçildi", Toast.LENGTH_SHORT).show()
            } else if (requestCode == 9002 && data.data != null) {
                selectedAudioUri = data.data!!
                audioSelected = true
                Toast.makeText(this, "Ses dosyası seçildi", Toast.LENGTH_SHORT).show()
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