package com.dhruvlimbachiya.pokedexappcompose.ui.pokemon_list_screen

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.bitmap.BitmapPool
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
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

            PokemonList(navHostController = navHostController)

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
 * Display list of pokemon
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonList(
    navHostController: NavHostController,
    viewModel: PokedexViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }
    val endReached by remember { viewModel.endReached }

    // Create a grid list
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(pokemonList.size) {
            // Paginate if it at last element of the list but end of the list is not arrived yet.
            if (!endReached && it >= pokemonList.size - 1 && !isLoading) {
                viewModel.loadPokemonPaginatedList()
            }
            PokedexEntry(pokedexListEntry = pokemonList[it], navHostController = navHostController)
        }
    }

    // Box for displaying progress bar or Retry section
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadPokemonPaginatedList()
            }
        }
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
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                navHostController.navigate(
                    Screen.PokemonDetailScreen.withArgs(
                        dominantColor.toArgb(),
                        pokedexListEntry.name
                    )
                )
            }
    ) {

        // Create and config painter.
        val painter = rememberImagePainter(
            data = pokedexListEntry.imageUrl, // Request the data from the image url
            builder = {
                crossfade(true) // Enable cross-fade animation when images are successfully loaded.
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

        Column {
            // Pokemon image.
            Image(
                painter = painter,
                contentDescription = pokedexListEntry.name,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )

            // Display Pokemon name
            Text(
                text = pokedexListEntry.name,
                fontSize = 20.sp,
                fontFamily = RobotoCondensed,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        when (painter.state) {
            // Display progress bar on image loading.
            is ImagePainter.State.Loading -> {
                CircularProgressIndicator()
            }
            else -> {/*NO OP*/
            }
        }

    }
}

@Composable
fun RetrySection(
    modifier: Modifier = Modifier,
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(
            text = error,
            fontSize = 40.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                onRetry()
            },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}