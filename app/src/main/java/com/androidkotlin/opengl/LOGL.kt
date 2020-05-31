@file:Suppress(
    "unused",
    "unused_variable",
    "unused_parameter",
    "deprecation",
    "UNUSED_ANONYMOUS_PARAMETER",
    "UNUSED_EXPRESSION",
    "MemberVisibilityCanBePrivate", "FunctionWithLambdaExpressionBody",
    "UnusedMainParameter")
package com.androidkotlin.opengl

import android.app.Application
import timber.log.Timber

// initialize the Timber logger.
// FMI: see  https://github.com/JakeWharton/timber
class LOGL : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

