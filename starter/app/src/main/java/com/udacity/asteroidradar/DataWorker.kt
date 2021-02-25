package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.HttpException

class DataWorker(appContext: Context, params: WorkerParameters) :
        CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "DataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val asteroidsRepository = (applicationContext as App).asteroidsRepository
            asteroidsRepository.fetchAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}