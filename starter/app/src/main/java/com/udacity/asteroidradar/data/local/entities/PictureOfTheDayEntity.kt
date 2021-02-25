package com.udacity.asteroidradar.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_of_the_day")
data class PictureOfTheDayEntity(
        @ColumnInfo(name = "media_type")
        val mediaType: String,
        val title: String,
        @PrimaryKey
        val url: String
)