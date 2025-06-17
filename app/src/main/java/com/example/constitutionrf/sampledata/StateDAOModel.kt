package com.example.constitutionrf.sampledata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StateDAOModel {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProject(stateModel: StateModel)

    @Query("SELECT * FROM project_table WHERE num = :number LIMIT 1")
    fun findByNum(number: Int): StateModel?
    @Update
    fun updateProject(stateModel: StateModel)
    @Query("SELECT * FROM project_table ORDER BY num ASC")
    fun getAllProjects(): List<StateModel>
}