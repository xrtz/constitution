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

class StateViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabaseModel.getDatabase(application)
    private val repositoryModel = StateRepositoryModel(db.stateDAO())

    fun addProject(stateModel: StateModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.addProject(stateModel = stateModel)
        }
    }
    fun updateProject(stateModel: StateModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.updateProject(stateModel)
        }
    }
    fun getAllProjects(callback: (List<StateModel>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val all = db.stateDAO().getAllProjects()
            withContext(Dispatchers.Main) {
                callback(all)
            }
        }
    }
    fun findByNumAsync(num: Int, callback: (StateModel?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repositoryModel.findByNum(num)
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }


}