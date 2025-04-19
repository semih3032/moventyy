// LoadingScreen.kt
package com.example.moventy.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingScreen(isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}

fun showToast(message: String) {
    // Toast mesajını burada gösterebilirsiniz
    println("Toast: $message")
}

fun runFFmpegCommand(command: String) {
    try {
        // FFmpeg komutu çalıştırılır
        val process = Runtime.getRuntime().exec(command)
        val result = process.waitFor()
        if (result == 0) {
            // Başarılı işlem
            showToast("Video başarıyla dışa aktarıldı!")
        } else {
            // Hata durumu
            showToast("Bir hata oluştu!")
        }
    } catch (e: Exception) {
        showToast("Bir hata oluştu: ${e.message}")
    }
}
