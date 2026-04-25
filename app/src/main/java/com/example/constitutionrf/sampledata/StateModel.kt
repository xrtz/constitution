package com.example.constitutionrf.sampledata

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Модель данных для хранения статьи Конституции Российской Федерации.
 *
 * Является сущностью Room, хранится в таблице `project_table`.
 * Поле [num] имеет уникальный индекс — это гарантирует отсутствие дублей
 * при повторном предзаполнении через [MainActivity].
 *
 * @property id Уникальный идентификатор записи (автоинкремент, по умолчанию 0).
 * @property num Порядковый номер статьи Конституции (от 1 до 137).
 * @property text Текст статьи. До загрузки содержит значение «Статья не найдена».
 * @property favourites Флаг нахождения статьи в избранном у пользователя.
 */
@Entity(tableName = "project_table", indices = [Index(value = ["num"], unique = true)])
data class StateModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val num: Int,
    val text: String,
    val favourites: Boolean
)