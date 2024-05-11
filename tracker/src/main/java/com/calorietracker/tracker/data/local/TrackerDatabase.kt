package com.calorietracker.tracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.calorietracker.tracker.data.local.entity.TrackedFoodEntity

@Database(
    entities = [TrackedFoodEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TrackerDatabase : RoomDatabase() {

    abstract val dao: TrackerDao
}