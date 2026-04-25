package com.example.constitutionrf.sampledata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * DAO (Data Access Object) для работы с таблицей `project_table`.
 *
 * Предоставляет методы для добавления, обновления и выборки объектов [StateModel]
 * из локальной базы данных Room. Все методы вызываются из фоновых потоков
 * через [StateRepositoryModel] и [StateViewModel].
 */
@Dao
interface StateDAOModel {

    /**
     * Добавляет новую запись статьи в таблицу.
     * При конфликте по уникальному полю [StateModel.num] запись игнорируется
     * (стратегия [OnConflictStrategy.IGNORE]), что предотвращает дублирование
     * при повторной инициализации в [MainActivity].
     *
     * @param stateModel Объект статьи для добавления.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProject(stateModel: StateModel)

    /**
     * Ищет статью в таблице по её порядковому номеру.
     *
     * @param number Номер статьи Конституции (от 1 до 137).
     * @return Объект [StateModel] если найден, или `null` если запись отсутствует.
     */
    @Query("SELECT * FROM project_table WHERE num = :number LIMIT 1")
    fun findByNum(number: Int): StateModel?

    /**
     * Обновляет существующую запись статьи в таблице.
     * Используется для сохранения загруженного текста и изменения состояния избранного.
     *
     * @param stateModel Объект статьи с обновлёнными данными.
     */
    @Update
    fun updateProject(stateModel: StateModel)

    /**
     * Возвращает все статьи из таблицы, отсортированные по номеру по возрастанию.
     *
     * @return Список всех объектов [StateModel], упорядоченных по [StateModel.num].
     */
    @Query("SELECT * FROM project_table ORDER BY num ASC")
    fun getAllProjects(): List<StateModel>
}