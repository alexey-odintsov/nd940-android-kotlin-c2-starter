package com.udacity.asteroidradar.data

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay

interface AsteroidsRepository {
    enum class Filter { TODAY, WEEK, SAVED }

    fun getFilteredAsteroids(filter: Filter = Filter.TODAY): LiveData<List<Asteroid>>

    suspend fun getPictureOfTheDay(): PictureOfDay

    suspend fun fetchAsteroids(filter: Filter = Filter.TODAY)
}

