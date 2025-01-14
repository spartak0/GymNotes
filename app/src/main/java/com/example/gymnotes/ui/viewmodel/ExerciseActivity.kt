package com.example.gymnotes.ui.viewmodel

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymnotes.data.Exercise
import com.example.gymnotes.databinding.ActivityExerciseBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExerciseActivity : AppCompatActivity() {

    private val exerciseViewModel: ExerciseViewModel by viewModel()
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exerciseAdapter = ExerciseAdapter(
            onEdit = { exercise -> editExercise(exercise) },
            onDelete = { exercise -> deleteExercise(exercise) }
        )

        binding.recyclerViewExercises.apply {
            layoutManager = LinearLayoutManager(this@ExerciseActivity)
            adapter = exerciseAdapter
        }

        binding.buttonAddExercise.setOnClickListener {
            addExercise()
        }

        lifecycleScope.launch {
            exerciseViewModel.allExercises.collect { exercises ->
                exerciseAdapter.submitList(exercises)
            }
        }

        exerciseAdapter = ExerciseAdapter(
            onEdit = { exercise -> editExercise(exercise) },
            onDelete = { exercise ->
                exerciseViewModel.deleteExercise(exercise) // Удаление через ViewModel
                Toast.makeText(this, "Упражнение удалено: ${exercise.name}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun addExercise() {
        val dialog = ExerciseDialogFragment(
            workoutId = getIntentWorkoutId(), // Получаем workoutId
            onSave = { exercise ->
                exerciseViewModel.insertExercise(exercise) // Добавление через ViewModel
                Toast.makeText(this, "Упражнение добавлено", Toast.LENGTH_SHORT).show()
            }
        )
        dialog.show(supportFragmentManager, "ExerciseDialog")
    }

    private fun editExercise(exercise: Exercise) {
        val dialog = ExerciseDialogFragment(
            exercise = exercise,
            workoutId = exercise.workoutId, // Передаем workoutId текущего упражнения
            onSave = { updatedExercise ->
                exerciseViewModel.updateExercise(updatedExercise) // Обновление через ViewModel
                Toast.makeText(this, "Упражнение обновлено", Toast.LENGTH_SHORT).show()
            }
        )
        dialog.show(supportFragmentManager, "ExerciseDialog")
    }

    // Функция для получения workoutId из Intent
    private fun getIntentWorkoutId(): Int {
        return intent?.getIntExtra("workoutId", -1) ?: -1
    }

    private fun deleteExercise(exercise: Exercise) {
        exerciseViewModel.deleteExercise(exercise)
        Toast.makeText(this, "Упражнение удалено", Toast.LENGTH_SHORT).show()
    }
}
