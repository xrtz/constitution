package com.example.constitutionrf.sampledata

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.constitutionrf.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel для управления данными статей Конституции.
 *
 * Служит посредником между UI-компонентами ([MainActivity], [ListFragment], [StateFragment])
 * и репозиторием данных ([StateRepositoryModel]).
 *
 * Все операции с БД выполняются в [viewModelScope] на [Dispatchers.IO].
 * Результаты, требующие обновления UI, возвращаются в главный поток
 * [Dispatchers.Main] через функции-коллбэки.
 *
 * @param application Контекст приложения, необходимый для инициализации [AppDatabaseModel].
 */
class StateViewModel(application: Application) : AndroidViewModel(application) {

    /** Экземпляр базы данных, полученный через Singleton [AppDatabaseModel.getDatabase]. */
    private val db = AppDatabaseModel.getDatabase(application)

    /** Репозиторий — промежуточный слой между ViewModel и DAO. */
    private val repositoryModel = StateRepositoryModel(db.stateDAO())

    /**
     * Добавляет новую запись статьи в базу данных.
     * Выполняется асинхронно в [Dispatchers.IO] через [viewModelScope].
     *
     * @param stateModel Объект статьи для добавления.
     */
    fun addProject(stateModel: StateModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.addProject(stateModel = stateModel)
        }
    }

    /**
     * Обновляет существующую запись статьи в базе данных.
     * Выполняется асинхронно в [Dispatchers.IO] через [viewModelScope].
     *
     * @param stateModel Объект статьи с обновлёнными данными.
     */
    fun updateProject(stateModel: StateModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.updateProject(stateModel)
        }
    }

    /**
     * Асинхронно загружает список всех статей и передаёт результат в коллбэк
     * в главном потоке [Dispatchers.Main].
     *
     * @param callback Функция обратного вызова, получающая список всех объектов [StateModel].
     */
    fun getAllProjects(callback: (List<StateModel>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val all = db.stateDAO().getAllProjects()
            withContext(Dispatchers.Main) {
                callback(all)
            }
        }
    }

    /**
     * Асинхронно ищет статью по номеру и передаёт результат в коллбэк
     * в главном потоке [Dispatchers.Main].
     *
     * @param num Номер статьи Конституции (от 1 до 137).
     * @param callback Функция обратного вызова, получающая найденный [StateModel] или `null`.
     */
    fun findByNumAsync(num: Int, callback: (StateModel?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repositoryModel.findByNum(num)
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }
}