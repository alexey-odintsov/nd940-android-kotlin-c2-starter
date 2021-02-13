package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class ApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

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
                val result =
                    parseAsteroidsJsonResult(JSONObject(NASAApi.RETROFIT_SERVICE.getAsteroids()))
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
}