package com.moventy.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.moventy.R
import com.moventy.utils.TextStyleManager
import com.moventy.utils.FFmpegCommandRunner
import java.io.File

class AddTextActivity : AppCompatActivity() {

    private lateinit var btnPickVideo: Button
    private lateinit var btnApplyText: Button
    private lateinit var editText: EditText
    private lateinit var spinnerStyles: Spinner
    private var selectedTextStyle: String = ""
    private lateinit var selectedVideoUri: Uri
    private var videoSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_text)

        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnApplyText = findViewById(R.id.btnApplyText)
        editText = findViewById(R.id.editTextContent)
        spinnerStyles = findViewById(R.id.spinnerTextStyles)

        val styles = TextStyleManager.getTextStyles(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, styles.map { it.name })
        spinnerStyles.adapter = adapter

        spinnerStyles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedTextStyle = styles[position].command
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 3001)
        }

        btnApplyText.setOnClickListener {
            if (!videoSelected) {
                Toast.makeText(this, "Lütfen önce bir video seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textContent = editText.text.toString()
            if (textContent.isEmpty()) {
                Toast.makeText(this, "Yazı içeriği boş olamaz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val output = File(cacheDir, "text_output.mp4")
            val inputPath = getPathFromUri(selectedVideoUri)
            val drawTextFilter = "-vf drawtext=text='$textContent':$selectedTextStyle"
            val command = "-i $inputPath $drawTextFilter -codec:a copy ${output.absolutePath}"

            FFmpegCommandRunner.runCommand(
                command,
                onSuccess = {
                    Toast.makeText(this, "Yazı başarıyla eklendi!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(this, "Hata: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3001 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                selectedVideoUri = it
                videoSelected = true
                Toast.makeText(this, "Video seçildi", Toast.LENGTH_SHORT).show()
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