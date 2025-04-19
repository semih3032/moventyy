package com.moventy.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.moventy.R

class EditorActivity : AppCompatActivity() {

    private lateinit var applyEffectButton: Button
    private lateinit var exportButton: Button
    private lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        // Video URI kontrolü
        val videoUriString = intent.getStringExtra("videoUri")
        if (videoUriString == null) {
            Toast.makeText(this, "Video URI alınamadı!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        videoUri = Uri.parse(videoUriString)

        // Button'ları bağlama
        applyEffectButton = findViewById(R.id.btn_apply_effect)
        exportButton = findViewById(R.id.btn_export)

        // Efekt uygulama işlemi
        applyEffectButton.setOnClickListener {
            Toast.makeText(this, "Efekt uygulanacak (örnek)", Toast.LENGTH_SHORT).show()
            // Burada video efekti işlemi çağrılır
        }

        // Video dışa aktarma işlemi
        exportButton.setOnClickListener {
            Toast.makeText(this, "Video dışa aktarılıyor (örnek)", Toast.LENGTH_SHORT).show()
            // Burada FFmpeg işlemi başlatılır
        }
    }
}