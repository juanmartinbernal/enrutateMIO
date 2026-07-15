package com.enrutatemio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.enrutatemio.core.designsystem.theme.EnrutateMioTheme
import com.enrutatemio.navigation.EnrutateMioApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnrutateMioTheme {
                EnrutateMioApp()
            }
        }
    }
}
