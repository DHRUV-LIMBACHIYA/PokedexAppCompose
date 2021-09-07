package com.dhruvlimbachiya.pokedexappcompose.util

/**
 * Created by Dhruv Limbachiya on 07-09-2021.
 */

sealed class Resource<T>(val data: T? = null,val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String,data: T? = null) : Resource<T>(message = message,data = data)
    class Loading<T>() : Resource<T>()
}
