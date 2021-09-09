package com.dhruvlimbachiya.pokedexappcompose.ui.screens.pokeomdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.dhruvlimbachiya.pokedexappcompose.data.remote.responses.Pokemon
import com.dhruvlimbachiya.pokedexappcompose.util.Resource

/**
 * Created by Dhruv Limbachiya on 09-09-2021.
 */

@Composable
fun PokemonDetailScreen(
    navHostController: NavHostController,
    dominantColor: Color,
    pokemonName: String,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp
) {
    val pokemon = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfoFromRepository(pokemonName)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
    ) {
        DetailTopAppBarSection(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            navHostController = navHostController
        )

        PokemonInfoSection(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(6.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            progressbarModifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            pokemon = pokemon
        )

        if (pokemon is Resource.Success) {
            PokemonImage(
                modifier = Modifier
                    .fillMaxSize(),
                pokemon = pokemon,
                topPadding = topPadding,
                pokemonImageSize = pokemonImageSize
            )
        }
    }
}

/**
 * Composable for displaying AppBar.
 */
@Composable
fun DetailTopAppBarSection(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
            .offset(16.dp, 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back arrow",
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    navHostController.popBackStack() // Press Back to the PostListScreen
                },
            tint = Color.White
        )
    }
}

/**
 * Display pokemon image.
 */
@Composable
fun PokemonImage(
    modifier: Modifier = Modifier,
    pokemonImageSize: Dp,
    pokemon: Resource<Pokemon>,
    topPadding: Dp
) {
    pokemon.data?.sprites?.let {
        val painter = rememberImagePainter(
            data = it.front_default, // data to load
            builder = {
                crossfade(true) // Enable cross fade animation.
            }
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = modifier
        ) {
            Image(
                painter = painter,
                contentDescription = pokemon.data.name,
                modifier = Modifier
                    .size(pokemonImageSize)
                    .offset(y = topPadding)
            )
        }
    }
}

/**
 * Display Pokemon details
 */
@Composable
fun PokemonInfoSection(
    modifier: Modifier = Modifier,
    progressbarModifier: Modifier,
    pokemon: Resource<Pokemon>
) {
    when (pokemon) {
        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = progressbarModifier
                    .size(100.dp)
            )
        }
        is Resource.Success -> {

        }
        is Resource.Error -> {
            Text(
                text = pokemon.message!!,
                color = Color.Red,
                modifier = modifier,
                textAlign = TextAlign.Center
            )
        }
    }

}