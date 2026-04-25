package com.example.constitutionrf.sampledata

/**
 * Репозиторий для работы с данными статей Конституции.
 *
 * Является промежуточным слоем между [StateViewModel] и [StateDAOModel].
 * Инкапсулирует логику доступа к источнику данных, упрощая замену
 * реализации БД без изменения ViewModel.
 *
 * @param stateDAOModel DAO для выполнения операций с базой данных.
 */
class StateRepositoryModel(private val stateDAOModel: StateDAOModel) {

    /**
     * Добавляет новую запись статьи в базу данных.
     * При конфликте по уникальному номеру запись игнорируется.
     *
     * @param stateModel Объект статьи для добавления.
     */
    fun addProject(stateModel: StateModel) {
        stateDAOModel.addProject(stateModel = stateModel)
    }

    /**
     * Ищет статью в базе данных по её порядковому номеру.
     *
     * @param num Номер статьи Конституции (от 1 до 137).
     * @return Объект [StateModel] если найден, или `null` если статья не существует.
     */
    fun findByNum(num: Int): StateModel? {
        return stateDAOModel.findByNum(num)
    }

    /**
     * Обновляет существующую запись статьи в базе данных.
     * Применяется для сохранения загруженного текста и изменения состояния избранного.
     *
     * @param stateModel Объект статьи с обновлёнными данными.
     */
    fun updateProject(stateModel: StateModel) {
        stateDAOModel.updateProject(stateModel)
    }
}