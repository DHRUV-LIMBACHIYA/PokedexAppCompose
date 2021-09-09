package com.dhruvlimbachiya.pokedexappcompose.ui.screens.pokeomdetail

import androidx.lifecycle.ViewModel
import com.dhruvlimbachiya.pokedexappcompose.data.remote.responses.Pokemon
import com.dhruvlimbachiya.pokedexappcompose.repository.PokemonRepository
import com.dhruvlimbachiya.pokedexappcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Dhruv Limbachiya on 09-09-2021.
 */

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    /**
     * Fetch the pokemon info by using its name.
     */
    suspend fun getPokemonInfoFromRepository(pokemonName: String): Resource<Pokemon> {
        return pokemonRepository.getPokemonInfo(pokemonName)
    }
}