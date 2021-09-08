package com.dhruvlimbachiya.pokedexappcompose.model

/**
 * Created by Dhruv Limbachiya on 08-09-2021.
 */

data class PokedexListEntry(
    val serialNumber: Int,
    val name: String,
    val imageUrl: String? = "",
)
