package com.example.gymnotes.ui.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymnotes.databinding.ItemExerciseBinding
import com.example.gymnotes.data.Exercise

class ExerciseAdapter(
    private val onEdit: (Exercise) -> Unit,
    private val onDelete: (Exercise) -> Unit
) : ListAdapter<Exercise, ExerciseAdapter.ExerciseViewHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    inner class ExerciseViewHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.textExerciseName.text = exercise.name
            binding.textExerciseDescription.text = exercise.description
            binding.buttonEdit.setOnClickListener { onEdit(exercise) }
            binding.buttonDelete.setOnClickListener { onDelete(exercise) }
        }
    }
}

class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean = oldItem == newItem
}
