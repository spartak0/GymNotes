package com.example.gymnotes.di

import android.content.Context
import androidx.room.Room
import com.example.gymnotes.data.GymNotesDatabase
import com.example.gymnotes.ui.viewmodel.ExerciseViewModel
import com.example.gymnotes.ui.viewmodel.WorkoutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(get(), GymNotesDatabase::class.java, "gymnotes_database")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<GymNotesDatabase>().workoutDao() }
    single { get<GymNotesDatabase>().exerciseDao() }

    // ViewModels с передачей Context
    viewModel { WorkoutViewModel(get()) } // Передаем Context в ViewModel
    viewModel { ExerciseViewModel(get()) }
}

