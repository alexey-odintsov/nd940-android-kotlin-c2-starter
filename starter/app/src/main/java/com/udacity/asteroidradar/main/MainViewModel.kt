package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.getDayFormatted
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.local.AsteroidDatabaseDao
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class ApiStatus { LOADING, ERROR, DONE }
enum class AsteroidFilter { WEEK, TODAY, SAVED }
class MainViewModel(val database: AsteroidDatabaseDao,
                    application: Application) : AndroidViewModel(application) {
    private var filter = AsteroidFilter.WEEK

    // Picture of the day
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    // Loading indicator
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    // Asteroids
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    // Navigate to details
    private val _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid>
        get() = _navigateToDetails

    init {
        getPictureOfTheDay()
        getAsteroids()
    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                val pod = NASAApi.RETROFIT_SERVICE.getPictureOfTheDay()
                Log.d("NASA", pod.toString())
                _pictureOfDay.value = pod
            } catch (e: Exception) {
                Log.e("NASA", "$e")
            }
        }
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                val dates = getFilterDates()
                val result = parseAsteroidsJsonResult(
                        JSONObject(NASAApi.RETROFIT_SERVICE.getAsteroids(dates.first, dates.second))
                )
                Log.d("NASA", "found ${result.size} elements")
                _asteroids.value = result
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                Log.e("NASA", "$e")
                _status.value = ApiStatus.ERROR
                _asteroids.value = null
            }
        }
    }

    private fun getFilterDates(): Pair<String, String> {
        val today = getDayFormatted(0)
        val todayPlus7 = getDayFormatted(DEFAULT_END_DATE_DAYS)
        return when (filter) {
            AsteroidFilter.TODAY -> Pair(today, today)
            else -> Pair(today, todayPlus7)
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToDetails.value = null
    }

    fun updateFilter(filter: AsteroidFilter) {
        if (this.filter != filter) {
            getAsteroids()
        }
        this.filter = filter
    }

    class Factory(
            private val dataSource: AsteroidDatabaseDao,
            private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}