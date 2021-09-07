package com.dhruvlimbachiya.pokedexappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.dhruvlimbachiya.pokedexappcompose.util.Constants.KEY_DOMINANT_COLOR
import com.dhruvlimbachiya.pokedexappcompose.util.Constants.KEY_POKEMON_NAME
import com.dhruvlimbachiya.pokedexappcompose.util.Screen
import com.dhruvlimbachiya.pokedexappcompose.ui.theme.JetpackComposePokedexTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposePokedexTheme {
                val navController = rememberNavController() // Get the NavController.
                Navigation(navController = navController)
            }
        }
    }
}

/**
 * Composable function responsible for hosting or navigating to other screen(composable).
 */
@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.PokemonListScreen.route) {
        // Composable for PokemonListScreen
        composable(route = Screen.PokemonListScreen.route) {

        }

        // Composable for PokemonDetailScreen.
        composable(
            // Route for navigating and passing argument to PokemonDetailScreen.
            route = Screen.PokemonDetailScreen.route + "/dominantColor/pokemonName",
            // list of arguments.
            arguments = listOf(
                navArgument(KEY_DOMINANT_COLOR) {
                    type = NavType.IntType
                },
                navArgument(KEY_POKEMON_NAME) {
                    type = NavType.StringType
                }
            )
        ) {
            val dominantColor = remember {
                val color =
                    it.arguments?.getInt(KEY_DOMINANT_COLOR)  // Get the value from the arguments
                color?.let { Color(it) } // Create a color object from the int value.
            }

            val pokemonName = remember {
                it.arguments?.getString(KEY_POKEMON_NAME) // Get the pokemon name from the arguments
            }
        }
    }
}
