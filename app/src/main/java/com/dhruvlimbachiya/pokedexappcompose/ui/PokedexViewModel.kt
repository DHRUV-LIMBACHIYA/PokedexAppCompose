package com.dhruvlimbachiya.pokedexappcompose.ui

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.dhruvlimbachiya.pokedexappcompose.model.PokedexListEntry
import com.dhruvlimbachiya.pokedexappcompose.repository.PokemonRepository
import com.dhruvlimbachiya.pokedexappcompose.util.Constants.PAGE_SIZE
import com.dhruvlimbachiya.pokedexappcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Dhruv Limbachiya on 08-09-2021.
 */

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    private var currentPage = 0
    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf("")
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokedexListEntry>() // Cached the pokemon list.
    var isSearching = mutableStateOf(false) // is currently searching or not.
    var isSearchBegan = true // Just began to start searching.

    init {
        loadPokemonPaginatedList()
    }

    /**
     * Search pokemon based on its name and it serial number
     */
    fun searchPokemon(query: String) {
        // Initially it will search from the Pokemon list(original) then afterwards it will search from the cachedPokemonList(duplicate)
        val listToSearch = if(isSearchBegan) {
            pokemonList.value
        } else {
            cachedPokemonList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                isSearching.value = false
                isSearchBegan = true
                pokemonList.value = cachedPokemonList // Display original list.
                return@launch
            }

            val result = listToSearch.filter { entry ->
                entry.name.contains(
                    query,
                    ignoreCase = true
                ) || entry.serialNumber.toString() == query.trim()
            }

            if (isSearchBegan) {
                cachedPokemonList = pokemonList.value // Duplicate the original list.
                isSearchBegan = false
            }

            pokemonList.value = result // Update the pokemon list by passing new search results.
            isSearching.value = true
    }
}

/**
 * Paginate the Pokemon list.
 */
fun loadPokemonPaginatedList() {
    viewModelScope.launch {

        isLoading.value = true

        val response = pokemonRepository.getPokemonListFromApi(
            limit = PAGE_SIZE,
            offset = currentPage * PAGE_SIZE
        )

        when (response) {
            is Resource.Success -> {
                endReached.value =
                    currentPage * PAGE_SIZE >= response.data?.count!! // Check if it reached at last element or not?

                val pokedexEntries = response.data.results.mapIndexed { index, entry ->
                    // It will get the number by removing trailing slash from the url.
                    val number = if (entry.url.endsWith("/")) {
                        entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                    } else {
                        entry.url.takeLastWhile { it.isDigit() }
                    }

                    // an image url for a particular pokemon.
                    val imageUrl =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                    // Create a pokedex entry.
                    PokedexListEntry(
                        number.toInt(),
                        entry.name.replaceFirstChar { it.uppercase(Locale.ROOT) },
                        imageUrl
                    )
                }

                currentPage++
                pokemonList.value += pokedexEntries // Add current list to the existing list.
                isLoading.value = false
                loadError.value = ""

            }
            is Resource.Error -> {
                loadError.value =
                    response.message ?: "Failed to load the data" // Set error message.
                isLoading.value = false
            }
            else -> { /*NO OP*/
            }
        }
    }
}

/**
 * Function for getting dominant from drawable using Palette library.
 */
fun getDominantColorFromDrawable(bitmap: Bitmap, onFinished: (Color) -> Unit) {
    val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    Palette.Builder(bmp).generate { palette ->
        palette?.dominantSwatch?.rgb?.let { colorValue ->
            onFinished(Color(colorValue))
        }
    }
}
}