package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.bclass.data.repository.BClassRepository
import com.example.bclass.ui.BClassApp
import com.example.ui.theme.BClassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val repository = remember { BClassRepository() }
            val isDarkMode by repository.isDarkMode.collectAsState()

            BClassTheme(darkTheme = isDarkMode) {
                BClassApp(
                    repository = repository,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
