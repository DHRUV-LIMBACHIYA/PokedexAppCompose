package com.dhruvlimbachiya.pokedexappcompose.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Dhruv Limbachiya on 07-09-2021.
 */

interface PokeApi {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    )

    @GET("pokemon/{name}")
    suspend fun getPokemon(
       @Path("name") name: String
    )
}