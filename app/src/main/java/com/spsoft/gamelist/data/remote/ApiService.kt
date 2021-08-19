package com.spsoft.gamelist.data.remote

import com.spsoft.gamelist.data.models.gamelist.Game
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("games")
    suspend fun getList(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int,
        @Query("key") key: String
    ): Game

    @FormUrlEncoded
    @GET("games?{link}")
    suspend fun getlistLink(
        @Path("link") link: String
    ):Game

companion object {

    private const val API_KEY = "ebe7f17643cd4c5e92176cc953c88700"
    private const val BASE_URL = "https://api.rawg.io/api/"
    private const val TIMEOUT_INTERVAL = 1L
    operator fun invoke(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(initOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    private val interceptor = Interceptor { chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return@Interceptor chain.proceed(request)
    }

    private fun initOkHttpClient(): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(interceptor)
        /*   .readTimeout(TIMEOUT_INTERVAL, TimeUnit.MINUTES)
           .connectTimeout(TIMEOUT_INTERVAL, TimeUnit.MINUTES)*/
        return okhttp.build()
    }
}
}



