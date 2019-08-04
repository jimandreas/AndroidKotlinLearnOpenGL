package com.androidkotlin.opengl

import android.app.Application
import android.content.Context
import timber.log.Timber

// initialize the Timber logger.
// FMI: see  https://github.com/JakeWharton/timber
class LOGL : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

