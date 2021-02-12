package com.udacity.asteroidradar

import com.squareup.moshi.Json

class Asteroids(val links: Links,
                @Json(name = "element_count")
                val elementCount: Int)
class Links(val next: String, val prev: String, val self: String)
