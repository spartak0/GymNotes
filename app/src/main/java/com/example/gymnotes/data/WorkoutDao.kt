package com.example.gymnotes.data

import android.database.Cursor
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    // Вставка тренировки
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)

    // Получение всех тренировок
    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): Flow<List<Workout>>

    // Удаление тренировки
    @Delete
    suspend fun deleteWorkout(workout: Workout)

    // Обновление тренировки
    @Update
    suspend fun updateWorkout(workout: Workout)

    // Получение всех тренировок через Cursor
    @Query("SELECT * FROM workout")
    fun getWorkoutsCursor(): Cursor

    // Удаление тренировки по ID
    @Query("DELETE FROM workout WHERE id = :id")
    suspend fun deleteWorkoutById(id: Int)

    // Получение всех упражнений для тренировки
    @Query("SELECT * FROM exercise WHERE workoutId = :workoutId ORDER BY date DESC")
    fun getExercisesForWorkout(workoutId: Int): Flow<List<Exercise>>
}

