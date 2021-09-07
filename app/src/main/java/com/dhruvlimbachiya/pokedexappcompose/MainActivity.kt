package com.dhruvlimbachiya.pokedexappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dhruvlimbachiya.pokedexappcompose.ui.theme.JetpackComposePokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposePokedexTheme {

            }
        }
    }
}

