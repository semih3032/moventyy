// VideoPreview.kt (Önizleme ekranı)
package com.example.moventy.ui

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VideoPreview(videoUri: Uri) {
    // Video playback için bir ExoPlayer veya benzeri kütüphane kullanılabilir
    // Örnek olarak, video oynatmak için bir basit Composable yapısı
    VideoPlayer(
        videoUri = videoUri,
        modifier = Modifier.fillMaxWidth().height(200.dp)
    )
}

@Composable
fun VideoPlayer(videoUri: Uri, modifier: Modifier = Modifier) {
    // ExoPlayer veya VideoView ile video oynatma kodu burada olacak
    Text(text = "Video Önizlemesi: $videoUri", modifier = modifier)
}
