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

class VideoCollageActivity : AppCompatActivity() {

    private lateinit var btnPickVideos: Button
    private lateinit var btnCreateCollage: Button
    private lateinit var selectedVideoUris: MutableList<Uri>
    private var videosSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_collage)

        btnPickVideos = findViewById(R.id.btnPickVideos)
        btnCreateCollage = findViewById(R.id.btnCreateCollage)
        selectedVideoUris = mutableListOf()

        btnPickVideos.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, 10001)
        }

        btnCreateCollage.setOnClickListener {
            if (!videosSelected) {
                Toast.makeText(this, "Lütfen önce videolar seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "video_collage_output.mp4")

            val inputVideos = selectedVideoUris.joinToString("|") { getPathFromUri(it) }
            val command = "-i ${inputVideos} -filter_complex \"[0:v][1:v]hstack=inputs=2[v];[v]vstack=inputs=2\" -map \"[v]\" ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Video kolajı başarıyla oluşturuldu!", Toast.LENGTH_SHORT).show()
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
            if (data.clipData != null) {
                // Handle multiple video selection
                for (i in 0 until data.clipData!!.itemCount) {
                    val videoUri = data.clipData!!.getItemAt(i).uri
                    selectedVideoUris.add(videoUri)
                }
                videosSelected = true
                Toast.makeText(this, "${selectedVideoUris.size} video seçildi", Toast.LENGTH_SHORT).show()
            } else if (data.data != null) {
                // Handle single video selection
                selectedVideoUris.add(data.data!!)
                videosSelected = true
                Toast.makeText(this, "1 video seçildi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String {
        return uri.path ?: ""
    }
}
