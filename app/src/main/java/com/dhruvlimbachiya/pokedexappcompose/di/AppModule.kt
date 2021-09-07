package com.dhruvlimbachiya.pokedexappcompose.di

import com.dhruvlimbachiya.pokedexappcompose.BuildConfig
import com.dhruvlimbachiya.pokedexappcompose.data.remote.PokeApi
import com.dhruvlimbachiya.pokedexappcompose.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Dhruv Limbachiya on 07-09-2021.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokeApi(): PokeApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.POKEMON_API_BASE_URL)
        .build()
        .create(PokeApi::class.java)


    @Singleton
    @Provides
    fun providePokemonRepository(pokeApi: PokeApi) = PokemonRepository(pokeApi)
}