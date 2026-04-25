package com.example.constitutionrf.sampledata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Абстрактный класс базы данных Room для приложения «Конституция РФ».
 *
 * Содержит единственную таблицу `project_table` для хранения статей ([StateModel]).
 * Реализует паттерн Singleton: единственный экземпляр создаётся через [getDatabase]
 * и переиспользуется во всём приложении.
 *
 * Аннотация [exportSchema] = false отключает экспорт схемы в файл при сборке.
 *
 * @see StateDAOModel
 * @see StateModel
 */
@Database(entities = [StateModel::class], version = 1, exportSchema = false)
abstract class AppDatabaseModel : RoomDatabase() {

    /**
     * Предоставляет доступ к DAO для выполнения операций со статьями Конституции.
     *
     * @return Экземпляр [StateDAOModel].
     */
    abstract fun stateDAO(): StateDAOModel

    companion object {

        /**
         * Единственный экземпляр базы данных.
         * Аннотация [@Volatile] гарантирует видимость изменений для всех потоков.
         */
        @Volatile
        private var INSTANCE: AppDatabaseModel? = null

        /**
         * Возвращает единственный экземпляр базы данных, создавая его при первом обращении.
         * Использует синхронизацию для безопасной работы в многопоточной среде.
         *
         * @param context Контекст приложения для создания базы данных.
         * @return Экземпляр [AppDatabaseModel].
         */
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