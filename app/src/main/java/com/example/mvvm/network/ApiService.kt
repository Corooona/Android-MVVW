package com.example.mvvm.network

import com.example.mvvm.model.ExerciseResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz que describe los endpoints de la API de Wger.
 *
 * Retrofit genera automáticamente la implementación en tiempo de ejecución.
 * Solo tienes que declarar las funciones — Retrofit hace la llamada HTTP real.
 *
 * La función es `suspend` para poder llamarla desde una Corrutina.
 */
interface ApiService {

    @GET("exerciseinfo/")
    suspend fun getExercises(
        @Query("language") language: Int = 2,       // 2 = inglés en la API de Wger
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 20
    ): ExerciseResponse
}

/**
 * Objeto singleton que crea y expone la instancia de Retrofit.
 *
 * Se usa `object` (singleton) para que solo exista una instancia
 * del cliente HTTP en toda la app — es costoso crearlo cada vez.
 */
object RetrofitInstance {

    private const val BASE_URL = "https://wger.de/api/v2/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // conecta Gson al converter
            .build()
            .create(ApiService::class.java)
    }
}
