package com.dhruvlimbachiya.pokedexappcompose.ui.screens.pokeomdetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.dhruvlimbachiya.pokedexappcompose.R
import com.dhruvlimbachiya.pokedexappcompose.data.remote.responses.Pokemon
import com.dhruvlimbachiya.pokedexappcompose.data.remote.responses.Type
import com.dhruvlimbachiya.pokedexappcompose.util.*
import java.util.*

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
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .align(Center),
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
                    .scale(0.5f)
            )
        }
        is Resource.Success -> {
            pokemon.data?.let { data ->
                PokemonDetails(
                    modifier = modifier
                        .fillMaxWidth()
                        .offset(y = 70.dp),
                    pokemon = data
                )
            }
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

/**
 * Composable for displaying Pokemon details like its name,type,weight,height & stats.
 */
@Composable
fun PokemonDetails(
    modifier: Modifier = Modifier,
    pokemon: Pokemon,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = CenterHorizontally,
    ) {
        Text(
            text = "#${pokemon.id} ${pokemon.name.replaceFirstChar { it.uppercase(Locale.ROOT) }}", // Ex :  #42 Goblat
            fontSize = 30.sp,
            color = MaterialTheme.colors.onSurface,
        )

        PokemonTypeSection(types = pokemon.types, modifier = Modifier.fillMaxWidth())

        PokemonWeightAndHeightSection(pokemon = pokemon)

        PokemonStatSection(pokemon)
    }
}

/**
 * Display pokemon type in circle shape box.
 */
@Composable
fun PokemonTypeSection(
    modifier: Modifier = Modifier,
    types: List<Type>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        for (type in types) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
                    .background(
                        parseTypeToColor(type),
                        CircleShape
                    ) // Choose bgColor based on type of pokemon.
                    .clip(CircleShape)
                    .height(35.dp),
                contentAlignment = Center
            ) {
                Text(
                    text = type.type.name.replaceFirstChar { it.uppercase(Locale.ROOT) }, // Make first letter capital.
                    color = Color.White,
                    fontSize = 18.sp,
                )
            }
        }
    }
}


/**
 * Display height and weight of Pokemon
 */
@Composable
fun PokemonWeightAndHeightSection(
    pokemon: Pokemon,
    sectionHeight: Dp = 80.dp
) {

    // Convert weight and height in float with one decimal digit.
    val weightInKg = remember {
        pokemon.weight / 10f
    }

    val heightInMeter = remember {
        pokemon.height / 10f
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {

        // Display weight data.
        PokemonDataItem(
            dataValue = weightInKg,
            dataUnit = "kg",
            image = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier
                .height(sectionHeight)
                .weight(1f)
        )

        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )

        // Display height data.
        PokemonDataItem(
            dataValue = heightInMeter,
            dataUnit = "m",
            image = painterResource(id = R.drawable.ic_height),
            modifier = Modifier
                .height(sectionHeight)
                .weight(1f)
        )
    }
}

@Composable
fun PokemonStatSection(
    pokemon: Pokemon
) {

    val maxStat = remember {
        pokemon.stats.maxOf { it.base_stat } // Returns the largest value among all values
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Basic Stats:",
            fontSize = 18.sp,
            color = MaterialTheme.colors.onSurface
        )
    }

    Spacer(modifier = Modifier.height(4.dp))

    for (i in pokemon.stats.indices) {
        PokeStatisticsData(
            statName = parseStatToAbbr(pokemon.stats[i]), // Parse the stat full name to abbreviate form
            statValue = pokemon.stats[i].base_stat , // base stat value of pokemon
            statMaxValue = findMaxStatValue(pokemon.stats[i].base_stat), // Max stat value among all pokemon
            statColor = parseStatToColor(pokemon.stats[i]), // Parse pokemon stat into color
            animationDelay = i * 100 // delay per stat item.
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}




@Composable
fun PokeStatisticsData(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    animationDelay: Int,
    animationDuration: Int = 1000,
    sectionHeight: Dp = 28.dp
) {

    // Is animation played or not.
    var isAnimated by remember {
        mutableStateOf(false)
    }

    val currentWidthPercent = animateFloatAsState(
        // if animation played the interpolate from 0f to statValue else stateValue => 0
        targetValue = if (isAnimated) {
            statValue / statMaxValue.toFloat()
        } else {
            0f
        },
        animationSpec = tween(
            durationMillis = animationDuration, // animation duration time
            delayMillis = animationDelay // animation delay time
        )
    )


    LaunchedEffect(key1 = true) {
        isAnimated = true // enable is animated once
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                },
                shape = CircleShape
            )
            .clip(CircleShape)
            .height(sectionHeight)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(currentWidthPercent.value)
                .clip(CircleShape)
                .background(statColor, CircleShape)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold
            )

            Text(
                // currentWidthPercent is in between 0f to 1f so to make it two digit number multiply with statMaxValue.
                // Ex: currentWidthPercent = 0.5 & statMaxValue = 100  => 0.5 * 100 = 50(Ans)
                text = if ((currentWidthPercent.value * statMaxValue).toInt() > 100) {
                    "100+"
                } else {
                    (currentWidthPercent.value * statMaxValue).toInt().toString()
                },
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


/**
 * A reusable composable for display height and weight with icon.
 */
@Composable
fun PokemonDataItem(
    modifier: Modifier = Modifier,
    dataValue: Float,
    dataUnit: String,
    image: Painter
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = image,
            contentDescription = "unit",
            tint = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colors.onSurface,
            fontSize = 16.sp
        )
    }

}

