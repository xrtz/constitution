package com.example.constitutionrf.sampledata

/**
 * Интерфейс обработчика нажатий на элементы списка статей.
 *
 * Используется адаптером [RecylerViewItem] для передачи события нажатия
 * во фрагмент [ListFragment], который выполняет навигацию к выбранной статье.
 */
interface OnItemClickListener {

    /**
     * Вызывается при нажатии пользователем на элемент списка.
     *
     * @param position 0-индексированная позиция нажатого элемента в списке.
     */
    fun onItemClick(position: Int)
}