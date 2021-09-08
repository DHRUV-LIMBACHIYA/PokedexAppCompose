package com.dhruvlimbachiya.pokedexappcompose.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.dhruvlimbachiya.pokedexappcompose.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Dhruv Limbachiya on 08-09-2021.
 */

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {


    /**
     * Function for getting dominant from drawable using Palette library.
     */
    fun getDominantColorFromDrawable(bitmap: Bitmap,onFinished: (Color) -> Unit) {
        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888,true)

        Palette.Builder(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinished(Color(colorValue))
            }
        }
    }
}