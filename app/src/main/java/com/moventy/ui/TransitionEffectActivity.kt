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
import com.example.moventy.effects.TransitionEffect
import com.example.moventy.effects.EffectsManager
import com.example.moventy.ui.theme.MoventyTheme

class TransitionEffectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Uygulama temasında içerik ayarlanıyor
        setContent {
            MoventyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TransitionEffectScreen()
                }
            }
        }
    }
}

@Composable
fun TransitionEffectScreen() {
    val effects = remember { EffectsManager.loadTransitionEffects() }
    var selectedEffect by remember { mutableStateOf<TransitionEffect?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Geçiş Efekti Seç", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(effects) { effect ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedEffect = effect },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedEffect == effect) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = effect.name,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                selectedEffect?.let {
                    // Uygulanacak FFmpeg komutunu oluşturur
                    val command = it.ffmpegCommand
                    // İlgili FFmpeg komutunu uygula
                    // Örn: FFmpegCommandRunner.run(command)
                }
            },
            enabled = selectedEffect != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Efekti Uygula")
        }
    }
}
