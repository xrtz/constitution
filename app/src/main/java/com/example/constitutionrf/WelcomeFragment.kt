package com.example.constitutionrf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

/**
 * Фрагмент экрана приветствия приложения «Конституция РФ».
 *
 * Является стартовым экраном, который отображается при первом запуске.
 * Содержит приветственное сообщение и кнопку «Перейти», при нажатии на
 * которую выполняется навигация к [ListFragment] со списком всех статей.
 */
class WelcomeFragment : Fragment() {

    /**
     * Вызывается при создании фрагмента до отрисовки View.
     *
     * @param savedInstanceState Сохранённое состояние фрагмента, если имеется.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Создаёт иерархию View фрагмента на основе [R.layout.fragment_welcome].
     *
     * @param inflater Объект для раздувания layout-ресурса.
     * @param container Родительский ViewGroup фрагмента.
     * @param savedInstanceState Сохранённое состояние, если имеется.
     * @return Корневой View фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    /**
     * Вызывается после создания View. Устанавливает обработчик нажатия
     * на кнопку «Перейти», которая переводит пользователя к [ListFragment].
     *
     * @param view Корневой View фрагмента.
     * @param savedInstanceState Сохранённое состояние, если имеется.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn: Button = view.findViewById(R.id.button_welcome)
        btn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_listFragment)
        }
    }

    companion object {
        /**
         * Фабричный метод для создания нового экземпляра [WelcomeFragment].
         *
         * @return Новый экземпляр [WelcomeFragment] без дополнительных аргументов.
         */
        @JvmStatic
        fun newInstance() =
            WelcomeFragment()
    }
}