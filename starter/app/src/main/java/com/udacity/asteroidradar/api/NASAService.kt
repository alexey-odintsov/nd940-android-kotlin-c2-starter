package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroids
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://api.nasa.gov/"

interface NASAService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(): Asteroids

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(): PictureOfDay
}

/**
 * Interceptor to add api_key parameter to every request of the service
 */
private class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val orig = chain.request()
        val url = orig.url().newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
        return chain.proceed(orig.newBuilder().url(url).build())
    }
}

/**
 * JSON converter
 */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .build())
        .build()

object NASAApi {
    val RETROFIT_SERVICE: NASAService by lazy { retrofit.create(NASAService::class.java) }
}