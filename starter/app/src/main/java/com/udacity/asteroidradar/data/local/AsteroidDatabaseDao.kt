package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity

@Dao
interface AsteroidDatabaseDao {
    @Insert
    suspend fun insert(asteroid: AsteroidEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewAsteroids(vararg asteroids: AsteroidEntity)

    @Update
    suspend fun update(asteroid: AsteroidEntity)

    @Query("SELECT * from asteroids WHERE id = :key")
    suspend fun get(key: Long): AsteroidEntity?

    @Query("SELECT * from asteroids WHERE close_approach_date = :today")
    suspend fun getTodayAsteroids(today: String): AsteroidEntity?

    @Query("SELECT * from asteroids WHERE close_approach_date >= :dateFrom AND close_approach_date < :dateTo")
    fun getAsteroids(dateFrom: String, dateTo: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids ORDER BY close_approach_date ASC")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids ORDER BY id DESC LIMIT 1")
    suspend fun getAsteroid(): AsteroidEntity?
}