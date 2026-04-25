package com.example.constitutionrf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.constitutionrf.sampledata.OnItemClickListener
import com.example.constitutionrf.sampledata.RecylerViewItem
import com.example.constitutionrf.sampledata.StateViewModel

/**
 * Фрагмент списка статей Конституции Российской Федерации.
 *
 * Отображает все 137 статей в виде прокручиваемого [RecyclerView].
 * Предоставляет поле ввода и кнопку для быстрого перехода к статье по её номеру.
 * При нажатии на элемент списка осуществляется навигация к [StateFragment].
 */
class ListFragment : Fragment() {

    /**
     * ViewModel для работы с данными статей Конституции.
     * Разделяется с другими фрагментами через область видимости активности.
     */
    private lateinit var stateViewModel: StateViewModel

    /**
     * Создаёт иерархию View фрагмента на основе [R.layout.fragment_list].
     *
     * @param inflater Объект для раздувания layout-ресурса.
     * @param container Родительский ViewGroup фрагмента.
     * @param savedInstanceState Сохранённое состояние, если имеется.
     * @return Корневой View фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    /**
     * Вызывается после создания View. Инициализирует RecyclerView, поле ввода
     * и кнопку поиска статьи по номеру.
     *
     * Логика поиска:
     * - Если поле не пустое и значение находится в диапазоне 1..137 — выполняется
     *   навигация к соответствующей статье через [StateFragment].
     * - Иначе на поле ввода отображается сообщение об ошибке.
     *
     * @param view Корневой View фрагмента.
     * @param savedInstanceState Сохранённое состояние, если имеется.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val imgbtn: ImageButton = view.findViewById(R.id.imageButton2)
        val textedit: EditText = view.findViewById(R.id.editTextNumber)
        imgbtn.setOnClickListener {
            if (textedit.text.isNotEmpty() && textedit.text.toString().toInt() in 1..137){
                val num = textedit.text.toString().toInt() - 1
                val action = ListFragmentDirections.actionListFragmentToStateFragment(num + 1)
                textedit.setText("")
                findNavController().navigate(action)
            }
            else {
                textedit.setError("Введите корректный номер статьи")
            }
        }
        stateViewModel = ViewModelProvider(requireActivity())[StateViewModel::class.java]
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        stateViewModel.getAllProjects { list ->
            recyclerView.adapter = RecylerViewItem(list, object : OnItemClickListener {
                /**
                 * Обрабатывает нажатие на элемент списка статей.
                 * Выполняет навигацию к [StateFragment] с номером выбранной статьи.
                 *
                 * @param position 0-индексированная позиция нажатого элемента.
                 */
                override fun onItemClick(position: Int) {
                    val action = ListFragmentDirections.actionListFragmentToStateFragment(position + 1)
                    findNavController().navigate(action)
                }
            }, stateViewModel)
        }
    }
}