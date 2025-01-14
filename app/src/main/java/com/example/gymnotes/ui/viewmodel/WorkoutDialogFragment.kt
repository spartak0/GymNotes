package com.example.gymnotes.ui.viewmodel

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.gymnotes.R
import com.example.gymnotes.data.Workout
import com.example.gymnotes.databinding.DialogWorkoutBinding
import java.text.SimpleDateFormat
import java.util.*

class WorkoutDialogFragment(
    private val workout: Workout? = null,
    private val onSave: (Workout) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogWorkoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogWorkoutBinding.inflate(LayoutInflater.from(context))

        // Заполнение полей для редактирования
        workout?.let {
            binding.editTextType.setText(it.type)
            binding.editTextDuration.setText(it.duration.toString())
            binding.editTextDate.setText(it.date)
        }

        // Настройка выбора даты через DatePicker
        binding.editTextDate.setOnClickListener {
            showDatePicker()
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(if (workout == null) "Добавить тренировку" else "Редактировать тренировку")
            .setView(binding.root)
            .setPositiveButton("Сохранить") { _, _ -> validateAndSaveWorkout() }
            .setNegativeButton("Отмена", null)
            .create()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate.time)
                binding.editTextDate.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun validateAndSaveWorkout() {
        val type = binding.editTextType.text.toString().trim()
        val duration = binding.editTextDuration.text.toString().trim()
        val date = binding.editTextDate.text.toString().trim()

        // Проверка на заполненность
        if (type.isEmpty()) {
            binding.editTextType.error = "Введите тип тренировки"
            return
        }

        if (duration.isEmpty() || duration.toIntOrNull() == null || duration.toInt() <= 0) {
            binding.editTextDuration.error = "Введите корректную продолжительность"
            return
        }

        if (date.isEmpty()) {
            binding.editTextDate.error = "Выберите дату"
            return
        }

        val parsedDate = try {
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(date)
        } catch (e: Exception) {
            null
        }

        if (parsedDate == null) {
            binding.editTextDate.error = "Неверный формат даты"
            return
        }

        // Создание объекта Workout
        val newWorkout = workout?.copy(
            type = type,
            duration = duration.toInt(),
            date = date
        ) ?: Workout(
            id = 0, // ID генерируется Room
            type = type,
            duration = duration.toInt(),
            date = date
        )

        // Передача данных в коллбэк
        onSave(newWorkout)
    }
}

