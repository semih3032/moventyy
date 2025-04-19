package com.example.moventy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moventy.effects.TextStyle
import com.example.moventy.effects.EffectsManager
import com.example.moventy.ui.theme.MoventyTheme

class TextStyleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoventyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TextStyleScreen()
                }
            }
        }
    }
}

@Composable
fun TextStyleScreen() {
    val styles = remember { EffectsManager.loadTextStyles() }
    var selectedStyle by remember { mutableStateOf<TextStyle?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Yazı Stili Seç", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(styles) { style ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedStyle = style },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedStyle == style) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = style.name,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                selectedStyle?.let {
                    // Uygulanacak FFmpeg komutunu oluşturur
                    val command = it.ffmpegCommand
                    // FFmpeg işlemi burada uygulanabilir
                }
            },
            enabled = selectedStyle != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Stili Uygula")
        }
    }
}
