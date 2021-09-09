package com.dhruvlimbachiya.pokedexappcompose.di

import com.dhruvlimbachiya.pokedexappcompose.BuildConfig
import com.dhruvlimbachiya.pokedexappcompose.data.remote.PokeApi
import com.dhruvlimbachiya.pokedexappcompose.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideHttpLogger() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder().apply{
        addInterceptor(httpLoggingInterceptor)
    }.build()

    @Singleton
    @Provides
    fun providePokeApi(okHttpClient: OkHttpClient): PokeApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.POKEMON_API_BASE_URL)
        .client(okHttpClient)
        .build()
        .create(PokeApi::class.java)


    @Singleton
    @Provides
    fun providePokemonRepository(pokeApi: PokeApi) = PokemonRepository(pokeApi)
}