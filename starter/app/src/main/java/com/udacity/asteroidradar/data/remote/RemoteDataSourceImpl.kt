package com.udacity.asteroidradar.data.remote

import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import org.json.JSONObject

class RemoteDataSourceImpl : RemoteDataSource {
    override suspend fun getPictureOfTheDay(): PictureOfDay {
        return NASAApi.RETROFIT_SERVICE.getPictureOfTheDay()
    }

    override suspend fun fetchAsteroids(dateFrom: String, dateTo: String): ArrayList<Asteroid> {
        val response = NASAApi.RETROFIT_SERVICE.getAsteroids(dateFrom, dateTo);
        return parseAsteroidsJsonResult(JSONObject(response))
    }
}