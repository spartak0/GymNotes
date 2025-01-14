package com.example.gymnotes.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymnotes.data.Workout
import com.example.gymnotes.data.WorkoutDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.launch
class WorkoutViewModel(private val context: Context) : ViewModel() {

    private val contentResolver = context.contentResolver

    // Flow для реактивного обновления данных
    private val _workoutsFlow = MutableStateFlow<List<Workout>>(emptyList())
    val workoutsFlow: StateFlow<List<Workout>> = _workoutsFlow

    init {
        loadAllWorkouts() // Загружаем тренировки при инициализации
    }

    // Загрузка всех тренировок через ContentProvider
    private fun loadAllWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            val workouts = mutableListOf<Workout>()
            val uri = Uri.parse("content://com.example.gymnotes.provider/workouts")
            val cursor = contentResolver.query(uri, null, null, null, null)

            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val type = it.getString(it.getColumnIndexOrThrow("type"))
                    val duration = it.getInt(it.getColumnIndexOrThrow("duration"))
                    val date = it.getString(it.getColumnIndexOrThrow("date"))
                    workouts.add(Workout(id, type, duration, date))
                }
            }
            _workoutsFlow.emit(workouts) // Обновляем Flow
        }
    }

    // Добавление тренировки
    fun addWorkout(type: String, duration: Int, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = Uri.parse("content://com.example.gymnotes.provider/workouts")
            val values = ContentValues().apply {
                put("type", type)
                put("duration", duration)
                put("date", date)
            }
            contentResolver.insert(uri, values)
            loadAllWorkouts() // Перезагружаем список тренировок
        }
    }

    // Удаление тренировки
    fun deleteWorkout(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = Uri.parse("content://com.example.gymnotes.provider/workouts/$id")
            contentResolver.delete(uri, null, null)
            loadAllWorkouts() // Перезагружаем список тренировок
        }
    }

    // Обновление тренировки
    fun updateWorkout(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = Uri.parse("content://com.example.gymnotes.provider/workouts/${workout.id}")
            val values = ContentValues().apply {
                put("type", workout.type)
                put("duration", workout.duration)
                put("date", workout.date)
            }
            contentResolver.update(uri, values, null, null)
            loadAllWorkouts() // Перезагружаем список тренировок
        }
    }
}


