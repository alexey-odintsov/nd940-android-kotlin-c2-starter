package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.data.AsteroidsRepository
import com.udacity.asteroidradar.data.AsteroidsRepositoryImpl
import com.udacity.asteroidradar.data.local.AsteroidDatabase
import com.udacity.asteroidradar.data.local.LocalDataSource
import com.udacity.asteroidradar.data.local.LocalDataSourceImpl
import com.udacity.asteroidradar.data.remote.RemoteDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class App : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private lateinit var localDataSource: LocalDataSource
    private val remoteDataSource = RemoteDataSourceImpl()
    lateinit var asteroidsRepository: AsteroidsRepository

    override fun onCreate() {
        super.onCreate()
        localDataSource = LocalDataSourceImpl(AsteroidDatabase.getInstance(this).asteroidDatabaseDao)
        asteroidsRepository = AsteroidsRepositoryImpl(localDataSource, remoteDataSource)
        initWorker()
    }

    private fun initWorker() {
        applicationScope.launch {
            setupWorker()
        }
    }

    private fun setupWorker() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED) // Wi-Fi
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<DataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                DataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest)
    }
}