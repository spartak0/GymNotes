package com.example.gymnotes.ui.workouts_screen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymnotes.data.Workout
import com.example.gymnotes.databinding.ActivityWorkoutBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.gymnotes.ui.workout_details_screen.WorkoutDetailsActivity
import com.example.gymnotes.ui.WorkoutAdapter


class WorkoutActivity : AppCompatActivity() {

    private val workoutViewModel: WorkoutViewModel by viewModel()
    private lateinit var workoutAdapter: WorkoutAdapter
    private lateinit var binding: ActivityWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workoutAdapter = WorkoutAdapter(
            onEdit = { workout -> editWorkout(workout) },
            onDelete = { workout -> deleteWorkout(workout) },
            onViewExercises = { workout ->
                val intent = Intent(this, WorkoutDetailsActivity::class.java).apply {
                    putExtra("workoutId", workout.id)
                }
                startActivity(intent)
            }
        )

        binding.recyclerViewWorkouts.apply {
            layoutManager = LinearLayoutManager(this@WorkoutActivity)
            adapter = workoutAdapter
        }

        binding.buttonAddWorkout.setOnClickListener {
            addWorkout()
        }

        // Подписка только на Flow
        lifecycleScope.launch {
            workoutViewModel.workoutsFlow.collect { workouts ->
                workoutAdapter.setWorkouts(workouts)
            }
        }
    }

    private fun addWorkout() {
        val dialog = WorkoutDialogFragment(onSave = { workout ->
            workoutViewModel.addWorkout(workout.type, workout.duration, workout.date)
            Toast.makeText(this, "Тренировка добавлена", Toast.LENGTH_SHORT).show()
        })
        dialog.show(supportFragmentManager, "WorkoutDialog")
    }

    private fun editWorkout(workout: Workout) {
        val dialog = WorkoutDialogFragment(workout, onSave = { updatedWorkout ->
            workoutViewModel.updateWorkout(updatedWorkout)
            Toast.makeText(this, "Тренировка обновлена", Toast.LENGTH_SHORT).show()
        })
        dialog.show(supportFragmentManager, "WorkoutDialog")
    }

    private fun deleteWorkout(workout: Workout) {
        workoutViewModel.deleteWorkout(workout.id)
        Toast.makeText(this, "Тренировка удалена: ${workout.type}", Toast.LENGTH_SHORT).show()
    }
}




