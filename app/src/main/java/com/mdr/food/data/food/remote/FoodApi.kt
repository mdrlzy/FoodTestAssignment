package com.mdr.food.data

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {

    @GET("3/search/movie?api_key=$API_KEY")
    suspend fun searchRepositories(
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResponsePagination<ResponseMovie>

    companion object {
        fun build(baseUrl: String): FoodApi {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(FoodApi::class.java)
        }
    }
}

const val API_KEY = "6ccd72a2a8fc239b13f209408fc31c33"

data class ResponsePagination<T>(
    val page: Int,
    val results: List<T>
)

data class ResponseMovie(
    val id: Long,
    val poster_path: String?,
    val title: String,
    val overview: String
) {
    fun resolvePosterUrl(): String? {
        return poster_path?.let { "https://image.tmdb.org/t/p/original$it" }
    }
}