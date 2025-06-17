package com.example.constitutionrf.sampledata

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "project_table", indices = [Index(value = ["num"], unique = true)])
data class StateModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val num: Int,
    val text: String,
    val favourites: Boolean
)
