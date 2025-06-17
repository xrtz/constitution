package com.example.constitutionrf.sampledata


class StateRepositoryModel(private val stateDAOModel: StateDAOModel) {

    fun addProject(stateModel: StateModel) {
        stateDAOModel.addProject(stateModel = stateModel)
    }

    fun findByNum(num: Int): StateModel? {
        return stateDAOModel.findByNum(num)
    }

    fun updateProject(stateModel: StateModel) {
        stateDAOModel.updateProject(stateModel)
    }

}