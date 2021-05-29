package com.abdalla.jetquotescompose.data

import android.app.Application
import com.abdalla.jetquotescompose.data.local.AppDatabase

class MainApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { MainRepository(database.getFavouritesDao()) }
}