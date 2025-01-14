package com.example.gymnotes.ui



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gymnotes.R
import com.example.gymnotes.data.Workout
import com.example.gymnotes.databinding.ItemWorkoutBinding

class WorkoutAdapter(
    private val onEdit: (Workout) -> Unit,
    private val onDelete: (Workout) -> Unit,
    private val onViewExercises: (Workout) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private val workouts = mutableListOf<Workout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(workouts[position])
    }

    override fun getItemCount(): Int = workouts.size

    fun setWorkouts(newWorkouts: List<Workout>) {
        workouts.clear()
        workouts.addAll(newWorkouts)
        notifyDataSetChanged()
    }

    inner class WorkoutViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout) {
            binding.textWorkoutType.text = workout.type
            binding.textWorkoutDuration.text = workout.duration.toString()
            binding.textWorkoutDate.text = workout.date
            binding.buttonEdit.setOnClickListener { onEdit(workout) }
            binding.buttonDelete.setOnClickListener { onDelete(workout) }
            binding.buttonViewExercises.setOnClickListener { onViewExercises(workout) }
        }
    }
}




// Callback для сравнения объектов Workout
class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
    override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean =
        oldItem == newItem
}
