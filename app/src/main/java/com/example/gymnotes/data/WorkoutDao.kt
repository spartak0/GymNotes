package com.example.gymnotes.data

import android.database.Cursor
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)

    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Query("SELECT * FROM workout")
    fun getWorkoutsCursor(): Cursor

    // Удаление тренировки по ID
    @Query("DELETE FROM workout WHERE id = :id")
    suspend fun deleteWorkoutById(id: Int)

    // Получение всех упражнений для тренировки
    @Query("SELECT * FROM exercise WHERE workoutId = :workoutId ORDER BY date DESC")
    fun getExercisesForWorkout(workoutId: Int): Flow<List<Exercise>>
}

