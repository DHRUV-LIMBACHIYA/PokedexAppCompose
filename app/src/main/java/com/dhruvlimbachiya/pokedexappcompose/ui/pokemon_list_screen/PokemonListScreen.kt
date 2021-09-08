package com.dhruvlimbachiya.pokedexappcompose.ui.pokemon_list_screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import coil.bitmap.BitmapPool
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.dhruvlimbachiya.pokedexappcompose.R
import com.dhruvlimbachiya.pokedexappcompose.model.PokedexListEntry
import com.dhruvlimbachiya.pokedexappcompose.ui.PokedexViewModel
import com.dhruvlimbachiya.pokedexappcompose.ui.theme.RobotoCondensed
import com.dhruvlimbachiya.pokedexappcompose.util.Screen

/**
 * Created by Dhruv Limbachiya on 07-09-2021.
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonListScreen(navHostController: NavHostController) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Spacer(modifier = Modifier.height(14.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "pokemon logo",
                modifier = Modifier.align(CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Searchbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

/**
 * Composable for displaying Searchbar
 */
@Composable
fun Searchbar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    // State holds text for Searchbar TextField.
    var searchQuery by remember {
        mutableStateOf("")
    }

    Box(modifier = modifier) {
        TextField(
            value = searchQuery,
            placeholder = {
                Text(text = "Search")
            },
            singleLine = true,
            maxLines = 1,
            onValueChange = {
                searchQuery = it
                onSearch(it)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.LightGray
            ),

            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, CircleShape)
                .background(color = Color.White, CircleShape)
                .padding(horizontal = 6.dp),
            shape = CircleShape
        )
    }
}


/**
 * Individual Pokedex Entry.
 */
@ExperimentalCoilApi
@Composable
fun PokedexEntry(
    modifier: Modifier = Modifier,
    pokedexListEntry: PokedexListEntry,
    viewModel: PokedexViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val defaultDominantColor =
        MaterialTheme.colors.surface  // Dark(black) color in Dark Theme and light(white) color in White Theme

    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        defaultDominantColor,
                        dominantColor
                    )
                )
            )
            .clickable {
            navHostController.navigate(Screen.PokemonDetailScreen.route + "/${dominantColor.toArgb()}/${pokedexListEntry.name}")
        }
    ) {
        Column {
            val painter = rememberImagePainter(
                data = pokedexListEntry.imageUrl, // Request the data from the image url
                builder = {
                    crossfade(true) // Enable cross-fade animation when images are successfully loaded.
                    placeholder(R.drawable.pokeball)
                    transformations(object : Transformation {
                        override fun key(): String {
                            return pokedexListEntry.imageUrl ?: ""
                        }

                        override suspend fun transform(
                            pool: BitmapPool,
                            input: Bitmap,
                            size: Size
                        ): Bitmap {
                            viewModel.getDominantColorFromDrawable(input) { color ->
                                dominantColor = color // Update the dominant color.
                            }
                            return input
                        }

                    })
                }
            )
            Image(
                painter = painter,
                contentDescription = pokedexListEntry.name,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )

            when (painter.state) {
                // Display progress bar on image loading.
                is ImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.scale(0.5f)
                    )
                }
                else -> {/*NO OP*/
                }
            }

            // Display Pokemon name
            Text(
                text = pokedexListEntry.name,
                fontSize = 20.sp,
                fontFamily = RobotoCondensed,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}