package com.example.moventy

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moventy.ui.theme.MoventyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Edge to edge özelliği açılıyor
        setContent {
            MoventyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),  // Logo resmini ekliyoruz
            contentDescription = "Moventy Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 32.dp)
        )

        // Başlık
        Text(text = "Moventy - Video Editor", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        // Video efekt ekleme butonu
        Button(
            onClick = { /* Video Effect Activity'ye geçiş */ },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Add Video Effect")
        }

        // Geçiş efekti ekleme butonu
        Button(
            onClick = { /* Transition Effect Activity'ye geçiş */ },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Add Transition Effect")
        }

        // Yazı stili ekleme butonu
        Button(
            onClick = { /* Text Style Activity'ye geçiş */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add Text Style")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoventyTheme {
        MainScreen()
    }
}