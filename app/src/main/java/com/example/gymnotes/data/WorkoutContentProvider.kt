package com.example.gymnotes.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.gymnotes.data.Workout
import kotlinx.coroutines.runBlocking

class WorkoutContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.gymnotes.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/workout")
    }

    private lateinit var database: GymNotesDatabase

    override fun onCreate(): Boolean {
        database = GymNotesDatabase.getInstance(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        // Получение данных через Cursor
        return database.workoutDao().getWorkoutsCursor()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val type = values?.getAsString("type") ?: return null
        val duration = values.getAsInteger("duration") ?: return null
        val date = values.getAsString("date") ?: return null // Ожидается Long вместо String

        val workout = Workout(type = type, duration = duration, date = date)

        // Синхронная вставка
        val id = runBlocking {
            database.workoutDao().insertWorkout(workout)
        }
        return Uri.withAppendedPath(CONTENT_URI, id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val id = uri.lastPathSegment?.toIntOrNull() ?: return 0

        runBlocking {
            database.workoutDao().deleteWorkoutById(id)
        }
        return 1
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val id = uri.lastPathSegment?.toIntOrNull() ?: return 0
        val type = values?.getAsString("type") ?: return 0
        val duration = values.getAsInteger("duration") ?: return 0
        val date = values.getAsString("date") ?: return 0 // Ожидается Long вместо String

        val workout = Workout(id = id, type = type, duration = duration, date = date)

        runBlocking {
            database.workoutDao().updateWorkout(workout)
        }
        return 1
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/vnd.$AUTHORITY.workout"
    }
}


