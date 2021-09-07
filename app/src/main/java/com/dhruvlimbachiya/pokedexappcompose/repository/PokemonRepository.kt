package com.dhruvlimbachiya.pokedexappcompose.repository

import com.dhruvlimbachiya.pokedexappcompose.data.remote.PokeApi
import com.dhruvlimbachiya.pokedexappcompose.data.remote.responses.Pokemon
import com.dhruvlimbachiya.pokedexappcompose.data.remote.responses.PokemonList
import com.dhruvlimbachiya.pokedexappcompose.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by Dhruv Limbachiya on 07-09-2021.
 */

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val pokeApi: PokeApi
){

    /**
     * Fetch the list of Pokemon from the PokeApi.
     */
    suspend fun getPokemonListFromApi(offset: Int,limit: Int) : Resource<PokemonList> {
        return try {
            val response = pokeApi.getPokemonList(offset, limit)
            Resource.Success(response)
        }catch (e: Exception) {
            Resource.Error("An Unknown error occured!")
        }
    }

    /**
     * Fetch an individual Pokemon info from the PokeApi.
     */
    suspend fun getPokemonInfo(name: String) : Resource<Pokemon> {
        return try {
            val response = pokeApi.getPokemon(name)
            Resource.Success(response)
        }catch (e: Exception) {
            Resource.Error("An Unknown error occured!")
        }
    }
}