package com.example.gymnotes.ui.viewmodel

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymnotes.data.Exercise
import com.example.gymnotes.databinding.ActivityWorkoutExercisesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.Flow

class WorkoutExercisesActivity : AppCompatActivity() {

    private val exerciseViewModel: ExerciseViewModel by viewModel()
    private lateinit var exerciseAdapter: ExerciseAdapter
    private var workoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWorkoutExercisesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workoutId = intent.getIntExtra("workoutId", 0)

        exerciseAdapter = ExerciseAdapter(
            onEdit = { exercise -> editExercise(exercise) },
            onDelete = { exercise -> deleteExercise(exercise) }
        )

        binding.recyclerViewExercises.apply {
            layoutManager = LinearLayoutManager(this@WorkoutExercisesActivity)
            adapter = exerciseAdapter
        }

        binding.buttonAddExercise.setOnClickListener {
            addExercise()
        }

        lifecycleScope.launch {
            exerciseViewModel.getExercisesForWorkout(workoutId).collect { exercises ->
                exerciseAdapter.submitList(exercises)
            }
        }
    }

    private fun addExercise() {
        val dialog = ExerciseDialogFragment(
            onSave = { exercise ->
                exerciseViewModel.insertExercise(exercise)
                Toast.makeText(this, "Упражнение добавлено", Toast.LENGTH_SHORT).show()
            },
            workoutId = workoutId // Передаём workoutId
        )
        dialog.show(supportFragmentManager, "ExerciseDialog")
    }

    private fun editExercise(exercise: Exercise) {
        val dialog = ExerciseDialogFragment(
            exercise = exercise,
            onSave = { updatedExercise ->
                exerciseViewModel.updateExercise(updatedExercise)
                Toast.makeText(this, "Упражнение обновлено", Toast.LENGTH_SHORT).show()
            },
            workoutId = workoutId // Передаём workoutId, чтобы сохранить связь
        )
        dialog.show(supportFragmentManager, "ExerciseDialog")
    }


    private fun deleteExercise(exercise: Exercise) {
        exerciseViewModel.deleteExercise(exercise)
        Toast.makeText(this, "Упражнение удалено", Toast.LENGTH_SHORT).show()
    }
}
