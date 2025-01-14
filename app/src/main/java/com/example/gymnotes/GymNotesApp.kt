package com.example.gymnotes

import android.app.Application
import com.example.gymnotes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GymNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Запуск Koin
        startKoin {
            androidContext(this@GymNotesApp)
            modules(appModule) // Подключение модуля
        }
    }
}