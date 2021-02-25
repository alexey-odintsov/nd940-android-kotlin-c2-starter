package com.udacity.asteroidradar.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.api.getDayFormatted
import com.udacity.asteroidradar.data.local.LocalDataSource
import com.udacity.asteroidradar.data.local.entities.asDomainModel
import com.udacity.asteroidradar.data.remote.RemoteDataSource
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.model.asDatabaseModel

class AsteroidsRepositoryImpl(private val localDataSource: LocalDataSource,
                              private val remoteDataSource: RemoteDataSource) : AsteroidsRepository {

    override fun getFilteredAsteroids(filter: AsteroidsRepository.Filter): LiveData<List<Asteroid>> {
        val dateFrom = getDayFormatted(0) // today
        val dateTo = getFilteredDateTo(filter)
        return localDataSource.getAsteroids(dateFrom, dateTo).map { it.asDomainModel() }
    }

    override suspend fun getPictureOfTheDay(): PictureOfDay {
        return remoteDataSource.getPictureOfTheDay()
    }

    override suspend fun fetchAsteroids(filter: AsteroidsRepository.Filter) {
        val dateFrom = getDayFormatted(0) // today
        val dateTo = getFilteredDateTo(filter)

        val newAsteroids = remoteDataSource.fetchAsteroids(dateFrom, dateTo)
        val newAsteroidsEntity = newAsteroids.asDatabaseModel().toTypedArray()
        localDataSource.updateAsteroids(newAsteroidsEntity)
    }

    private fun getFilteredDateTo(filter: AsteroidsRepository.Filter): String {
        return when (filter) {
            AsteroidsRepository.Filter.TODAY -> getDayFormatted(0)
            AsteroidsRepository.Filter.WEEK,
            AsteroidsRepository.Filter.SAVED -> getDayFormatted(DEFAULT_END_DATE_DAYS)
        }
    }
}