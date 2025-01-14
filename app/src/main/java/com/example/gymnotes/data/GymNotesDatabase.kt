package com.example.gymnotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Workout::class, Exercise::class], version = 4, exportSchema = false)
abstract class GymNotesDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao

        companion object {
        @Volatile
        private var INSTANCE: GymNotesDatabase? = null

        fun getInstance(context: Context): GymNotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymNotesDatabase::class.java,
                    "gymnotes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
