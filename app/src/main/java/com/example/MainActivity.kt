package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.HyperfuseViewModel
import com.example.ui.components.HyperfuseMainContent
import com.example.ui.theme.HyperfuseTheme

class MainActivity : ComponentActivity() {

    private val viewModel: HyperfuseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HyperfuseTheme {
                HyperfuseMainContent(viewModel = viewModel)
            }
        }
    }
}
