package com.example.gymnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymnotes.data.Exercise
import com.example.gymnotes.data.ExerciseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExerciseViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {

    val allExercises: Flow<List<Exercise>> = exerciseDao.getAllExercises()

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.insertExercise(exercise)
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.updateExercise(exercise)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.deleteExercise(exercise)
        }
    }

    fun getExercisesForWorkout(workoutId: Int): kotlinx.coroutines.flow.Flow<List<Exercise>> {
        return exerciseDao.getExercisesForWorkout(workoutId)
    }
}

