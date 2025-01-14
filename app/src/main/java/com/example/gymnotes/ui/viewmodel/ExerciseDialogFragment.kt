package com.example.gymnotes.ui.viewmodel

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.gymnotes.R
import com.example.gymnotes.data.Exercise
import com.example.gymnotes.databinding.FragmentExerciseDialogBinding
import java.text.SimpleDateFormat
import java.util.*
class ExerciseDialogFragment(
    private val exercise: Exercise? = null,
    private val workoutId: Int, // ID тренировки для связи
    private val onSave: (Exercise) -> Unit
) : DialogFragment() {

    private var _binding: FragmentExerciseDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        _binding = FragmentExerciseDialogBinding.inflate(inflater)

        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)

        setupDialog()
        return dialog
    }

    private fun setupDialog() {
        // Если редактируем существующее упражнение, заполняем поля
        exercise?.let {
            binding.editTextName.setText(it.name)
            binding.editTextDescription.setText(it.description)
        }

        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()

            if (validateInputs(name, description)) {
                val currentDate = System.currentTimeMillis()
                val newExercise = exercise?.copy(
                    name = name,
                    description = description,
                    date = currentDate
                ) ?: Exercise(
                    id = 0, // ID генерируется Room
                    name = name,
                    description = description,
                    date = currentDate,
                    workoutId = workoutId // Связь с тренировкой
                )

                onSave(newExercise)
                dismiss()
            }
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun validateInputs(name: String, description: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                Toast.makeText(context, getString(R.string.error_empty_name), Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(description) -> {
                Toast.makeText(context, getString(R.string.error_empty_description), Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
