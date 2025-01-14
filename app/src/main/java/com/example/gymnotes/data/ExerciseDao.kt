package com.example.gymnotes.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    // Получение всех упражнений, отсортированных по дате редактирования (date)
    @Query("SELECT * FROM exercise ORDER BY date DESC")
    fun getAllExercises(): Flow<List<Exercise>>

    // Вставка упражнения
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise): Long

    // Обновление упражнения
    @Update
    suspend fun updateExercise(exercise: Exercise)

    // Удаление упражнения
    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    // Получение всех упражнений для конкретной тренировки
    @Query("SELECT * FROM exercise WHERE workoutId = :workoutId ORDER BY date DESC")
    fun getExercisesForWorkout(workoutId: Int): Flow<List<Exercise>>
}
