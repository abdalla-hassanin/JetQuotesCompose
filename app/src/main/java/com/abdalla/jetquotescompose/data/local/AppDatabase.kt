package com.abdalla.jetquotescompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abdalla.jetquotescompose.data.Quote

@Database(entities = [Quote::class], version = 1,exportSchema =false )
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFavouritesDao(): FavouritesDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Jet-Quotes-new-ComposeDataBase"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}