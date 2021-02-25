package com.udacity.asteroidradar.data.remote

import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay

interface RemoteDataSource {
    suspend fun getPictureOfTheDay(): PictureOfDay
    suspend fun fetchAsteroids(dateFrom: String, dateTo: String): ArrayList<Asteroid>
}