package com.example.gymnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val duration: Int,
    val date: String
)
