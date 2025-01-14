package com.example.gymnotes.di

import androidx.room.Room
import com.example.gymnotes.data.GymNotesDatabase
import com.example.gymnotes.ui.exercise_screen.ExerciseViewModel
import com.example.gymnotes.ui.workouts_screen.WorkoutViewModel
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

    viewModel { WorkoutViewModel(get()) }
    viewModel { ExerciseViewModel(get()) }
}

