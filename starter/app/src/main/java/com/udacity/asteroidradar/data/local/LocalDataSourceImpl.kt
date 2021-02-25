package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity

class LocalDataSourceImpl(private val databaseDao: AsteroidDatabaseDao) : LocalDataSource {
    override fun getAllAsteroids(): LiveData<List<AsteroidEntity>> {
        return databaseDao.getAllAsteroids()
    }

    override fun updateAsteroids(newAsteroidsEntity: Array<AsteroidEntity>) {
        databaseDao.insertNewAsteroids(*newAsteroidsEntity)
    }

    override fun getAsteroids(dateFrom: String, dateTo: String): LiveData<List<AsteroidEntity>> {
        return databaseDao.getAsteroids(dateFrom, dateTo)
    }

}