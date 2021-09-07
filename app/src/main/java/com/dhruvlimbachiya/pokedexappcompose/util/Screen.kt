package com.dhruvlimbachiya.pokedexappcompose.util

import com.dhruvlimbachiya.pokedexappcompose.util.Constants.POKEMON_DETAIL_SCREEN_ROUTE
import com.dhruvlimbachiya.pokedexappcompose.util.Constants.POKEMON_LIST_SCREEN_ROUTE

/**
 * Created by Dhruv Limbachiya on 07-09-2021.
 */

/**
 * Screen sealed class for all navigation screens.
 */
sealed class Screen(val route: String) {
    object PokemonListScreen : Screen(POKEMON_LIST_SCREEN_ROUTE)
    object PokemonDetailScreen : Screen(POKEMON_DETAIL_SCREEN_ROUTE)

    /**
     * Helper function for building a route with arguments.
     */
    fun withArgs(vararg args: String) = buildString {
        append(POKEMON_DETAIL_SCREEN_ROUTE) // Route - main url
        args.forEach { arg ->
            append("/$arg") // append arguments on route.
        }

    }
}
