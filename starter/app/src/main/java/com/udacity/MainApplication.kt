package com.udacity

import android.app.Application
import timber.log.Timber

/**
 * Main entry point for the application. Timber logging is set up here and other
 * long running pre-execution tasks can be set up here as well.
 */
class MainApplication : Application() {
    /**
     * onCreate is called before the application passes execution off to the first activity.
     */
    override fun onCreate() {
        super.onCreate()
        // Plant LineNumberDebugTree
        Timber.plant(LineNumberDebugTree())
        Timber.d("Timber logging initialized.")
    }

    /**
     * Main class that is used by this application's Timber logging
     * Logs in the format:
     *
     * Level/(Filename.kt:LineNumber)#function: LogString
     */
    class LineNumberDebugTree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String? {
            return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
        }
    }
}