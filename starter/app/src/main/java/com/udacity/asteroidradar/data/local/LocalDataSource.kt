package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity

interface LocalDataSource {
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>
    fun updateAsteroids(newAsteroidsEntity: Array<AsteroidEntity>)
    fun getAsteroids(dateFrom: String, dateTo: String): LiveData<List<AsteroidEntity>>

}