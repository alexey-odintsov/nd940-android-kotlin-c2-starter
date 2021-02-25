package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.App
import com.udacity.asteroidradar.data.AsteroidsRepository
import com.udacity.asteroidradar.data.local.entities.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var filter = AsteroidsRepository.Filter.WEEK
    private val asteroidsRepository = (application as App).asteroidsRepository

    // Picture of the day
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    // Loading indicator
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    // Asteroids
    private val _asteroids = MediatorLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
    get() = _asteroids


    // Navigate to details
    private val _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid>
        get() = _navigateToDetails

    init {
        getPictureOfTheDay()
        viewModelScope.launch(Dispatchers.IO) {
            asteroidsRepository.fetchAsteroids()
//            _asteroids.addSource(asteroidsRepository.getFilteredAsteroids(filter))
        }
    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = asteroidsRepository.getPictureOfTheDay()
            } catch (e: Exception) {
                Log.e("NASA", "$e")
            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToDetails.value = null
    }

    fun showTodayAsteroids() {
        TODO("Not yet implemented")
    }

    fun showWeekAsteroids() {
        TODO("Not yet implemented")
    }

    fun showSavedAsteroids() {
        TODO("Not yet implemented")
    }

    class Factory(
            private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}