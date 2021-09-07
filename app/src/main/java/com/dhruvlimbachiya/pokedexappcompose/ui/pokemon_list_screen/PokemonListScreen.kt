package com.dhruvlimbachiya.pokedexappcompose.ui.pokemon_list_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dhruvlimbachiya.pokedexappcompose.R

/**
 * Created by Dhruv Limbachiya on 07-09-2021.
 */

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
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Searchbar(
                 modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

            }
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
                .padding(horizontal = 12.dp),
            shape = CircleShape
        )
    }
}