package com.moventy.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.moventy.R
import com.moventy.utils.EffectManager
import com.moventy.utils.FFmpegCommandRunner
import java.io.File

class VideoEffectActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var spinnerEffects: Spinner
    private lateinit var btnApplyEffect: Button
    private lateinit var selectedVideoUri: Uri
    private var videoSelected = false
    private lateinit var selectedEffect: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_effect)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        spinnerEffects = findViewById(R.id.spinnerEffects)
        btnApplyEffect = findViewById(R.id.btnApplyEffect)

        val effects = EffectManager.getVideoEffects(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, effects.map { it.name })
        spinnerEffects.adapter = adapter

        spinnerEffects.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedEffect = effects[position].command
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 2001)
        }

        btnApplyEffect.setOnClickListener {
            if (!videoSelected) {
                Toast.makeText(this, "Lütfen önce bir video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "effect_output.mp4")
            val inputPath = getPathFromUri(selectedVideoUri)
            val command = "-i $inputPath $selectedEffect ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Efekt başarıyla uygulandı!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(this, "Hata: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2001 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                selectedVideoUri = it
                videoSelected = true
                Toast.makeText(this, "Video seçildi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String {
        return uri.path ?: ""
    }
}
