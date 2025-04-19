package com.moventy.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LanguageSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        val btnTurkish = findViewById<Button>(R.id.btnTurkish)
        val btnEnglish = findViewById<Button>(R.id.btnEnglish)
        val btnFrench = findViewById<Button>(R.id.btnFrench)
        val btnSpanish = findViewById<Button>(R.id.btnSpanish)

        btnTurkish.setOnClickListener { setLanguage("tr") }
        btnEnglish.setOnClickListener { setLanguage("en") }
        btnFrench.setOnClickListener { setLanguage("fr") }
        btnSpanish.setOnClickListener { setLanguage("es") }
    }

    private fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val sharedPreferences = getSharedPreferences("MoventyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selected_language", languageCode)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
