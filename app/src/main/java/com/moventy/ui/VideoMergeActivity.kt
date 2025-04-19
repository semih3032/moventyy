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

class VideoMergeActivity : AppCompatActivity() {

    private lateinit var btnPickFirstVideo: Button
    private lateinit var btnPickSecondVideo: Button
    private lateinit var btnMergeVideos: Button
    private lateinit var selectedFirstVideoUri: Uri
    private lateinit var selectedSecondVideoUri: Uri
    private var firstVideoSelected = false
    private var secondVideoSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_merge)

        btnPickFirstVideo = findViewById(R.id.btnPickFirstVideo)
        btnPickSecondVideo = findViewById(R.id.btnPickSecondVideo)
        btnMergeVideos = findViewById(R.id.btnMergeVideos)

        btnPickFirstVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 5001)
        }

        btnPickSecondVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 5002)
        }

        btnMergeVideos.setOnClickListener {
            if (!firstVideoSelected || !secondVideoSelected) {
                Toast.makeText(this, "Lütfen iki video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "merged_video_output.mp4")
            val firstVideoPath = getPathFromUri(selectedFirstVideoUri)
            val secondVideoPath = getPathFromUri(selectedSecondVideoUri)

            val command = "-i $firstVideoPath -i $secondVideoPath -filter_complex \"[0:v][0:a][1:v][1:a]concat=n=2:v=1:a=1[v][a]\" -map \"[v]\" -map \"[a]\" ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Videolar başarıyla birleştirildi!", Toast.LENGTH_SHORT).show()
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
                5001 -> {
                    selectedFirstVideoUri = data.data!!
                    firstVideoSelected = true
                    Toast.makeText(this, "Birinci video seçildi", Toast.LENGTH_SHORT).show()
                }
                5002 -> {
                    selectedSecondVideoUri = data.data!!
                    secondVideoSelected = true
                    Toast.makeText(this, "İkinci video seçildi", Toast.LENGTH_SHORT).show()
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