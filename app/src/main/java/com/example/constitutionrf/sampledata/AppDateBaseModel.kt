package com.example.constitutionrf.sampledata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StateModel::class], version = 1, exportSchema = false)
abstract class AppDatabaseModel : RoomDatabase() {
    abstract fun stateDAO(): StateDAOModel
    companion object {
        @Volatile
        private var INSTANCE: AppDatabaseModel? = null

        fun getDatabase(context: Context): AppDatabaseModel {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabaseModel::class.java,
                    "state_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}