package com.example.constitutionrf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.constitutionrf.sampledata.StateModel
import com.example.constitutionrf.sampledata.StateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

/**
 * Фрагмент отображения отдельной статьи Конституции Российской Федерации.
 *
 * Получает номер статьи через навигационные аргументы Safe Args ([StateFragmentArgs]).
 *
 * При открытии проверяет локальный кеш (Room БД):
 * - Если текст закеширован — отображает его немедленно без сетевого запроса.
 * - Если в БД хранится заглушка «Статья не найдена» — запускает [fetchArticleText]
 *   в корутине на [Dispatchers.IO] и сохраняет полученный текст в БД.
 *
 * Управляет состоянием «Избранное» для статьи: изменения немедленно
 * сохраняются в локальной БД через [StateViewModel].
 */
class StateFragment : Fragment() {

    /**
     * ViewModel для работы с данными статей.
     * Разделяется с [MainActivity] и [ListFragment] через область видимости активности.
     */
    private lateinit var stateViewModel: StateViewModel

    /**
     * Навигационные аргументы, сгенерированные Safe Args.
     * Содержат номер статьи, переданный при навигации из [ListFragment].
     */
    private val args: StateFragmentArgs by navArgs()

    /**
     * Номер отображаемой статьи Конституции (от 1 до 137).
     * Извлекается из навигационных аргументов [args].
     */
    private val number: Int get() = args.NUMBER

    /**
     * Вызывается при создании фрагмента до отрисовки View.
     *
     * @param savedInstanceState Сохранённое состояние фрагмента, если имеется.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Создаёт иерархию View фрагмента на основе [R.layout.fragment_state].
     *
     * @param inflater Объект для раздувания layout-ресурса.
     * @param container Родительский ViewGroup фрагмента.
     * @param savedInstanceState Сохранённое состояние, если имеется.
     * @return Корневой View фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_state, container, false)
    }

    /**
     * Вызывается после создания View. Настраивает отображение статьи и логику избранного.
     *
     * Логика отображения текста:
     * - Если в БД текст == «Статья не найдена» → запускает [fetchArticleText] в корутине,
     *   отображает результат и сохраняет в БД через [StateViewModel.updateProject].
     * - Если текст уже загружен → отображает его напрямую.
     *
     * Логика избранного:
     * - `imb_fav` (звезда включена) — видима, если статья в избранном.
     * - `imb_fav_off` (звезда выключена) — видима, если статья не в избранном.
     * - Нажатие переключает состояние и сохраняет изменение в БД.
     *
     * @param view Корневой View фрагмента.
     * @param savedInstanceState Сохранённое состояние, если имеется.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvTitle: TextView = view.findViewById(R.id.tv_countOfState)
        val tvContent: TextView = view.findViewById(R.id.textView4)
        tvTitle.text = "Статья $number"
        stateViewModel = ViewModelProvider(requireActivity())[StateViewModel::class.java]
        val imb_fab: ImageButton = view.findViewById(R.id.imb_fav)
        val imb_fav_off: ImageButton = view.findViewById(R.id.imb_fav_off)

        // Восстановление состояния кнопки избранного из БД
        stateViewModel.findByNumAsync(number) { model ->
            model?.let {
                if(it.favourites == true) {
                    imb_fab.visibility = View.VISIBLE
                    imb_fav_off.visibility = View.INVISIBLE
                }
                else{
                    imb_fav_off.visibility = View.VISIBLE
                    imb_fab.visibility = View.INVISIBLE
                }
            }
        }

        // Загрузка текста: из кеша БД или с сайта constitution.ru
        stateViewModel.findByNumAsync(number) { model ->
            if (model?.text.equals("Статья не найдена")){
                lifecycleScope.launch {
                    val articleText = fetchArticleText(number)
                    val finalText = articleText ?: "Статья не найдена"
                    tvContent.text = finalText
                    model?.let {
                        val updated = it.copy(text = finalText)
                        stateViewModel.updateProject(updated)
                    }
                }
            }else {
                tvContent.text = model?.text ?: "Ошибка загрузки"
            }
        }

        // Добавление статьи в избранное
        imb_fav_off.setOnClickListener {
            stateViewModel.findByNumAsync(number) { model ->
                model?.let {
                    val updated = it.copy(favourites = true)
                    stateViewModel.updateProject(updated)
                }
                imb_fab.visibility = View.VISIBLE
                imb_fav_off.visibility = View.INVISIBLE
            }
        }

        // Удаление статьи из избранного
        imb_fab.setOnClickListener {
            stateViewModel.findByNumAsync(number) { model ->
                model?.let {
                    val updated = it.copy(favourites = false)
                    stateViewModel.updateProject(updated)
                }
                imb_fav_off.visibility = View.VISIBLE
                imb_fab.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        /**
         * Создаёт новый экземпляр [StateFragment] с указанным номером статьи.
         *
         * @param number Номер статьи Конституции (от 1 до 137).
         * @return Новый экземпляр [StateFragment] с установленными аргументами.
         */
        fun newInstance(number: Int): StateFragment {
            val fragment = StateFragment()
            val args = Bundle()
            args.putInt("NUMBER", number)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Загружает текст указанной статьи Конституции с сайта [constitution.ru](http://www.constitution.ru).
     *
     * Определяет URL нужной главы через [determineChapterUrl], затем парсит HTML-страницу
     * с помощью Jsoup: перебирает теги `<p>` и собирает параграфы, относящиеся
     * к запрошенной статье, до начала следующей.
     * Выполняется в фоновом потоке [Dispatchers.IO].
     *
     * @param articleNumber Номер статьи Конституции (от 1 до 137).
     * @return Текст статьи в виде строки, или `null` если загрузка завершилась ошибкой.
     */
    private suspend fun fetchArticleText(articleNumber: Int): String? {
        return withContext(Dispatchers.IO) {
            try {
                val chapterUrl = determineChapterUrl(articleNumber)
                val doc = Jsoup.connect(chapterUrl).get()
                val paragraphs = doc.select("p")

                val articleText = StringBuilder()
                var found = false

                for (paragraph in paragraphs) {
                    val text = paragraph.text()
                    if (text.startsWith("Статья $articleNumber")) {
                        found = true
                    } else if (found && text.startsWith("Статья")) {
                        break
                    } else if (found) {
                        articleText.append(text).append("\n")
                    }
                }

                if (articleText.isNotEmpty()) articleText.toString() else null
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Определяет URL страницы на сайте constitution.ru, содержащей указанную статью.
     *
     * Соответствие номеров статей и глав Конституции:
     * - Глава 1 (ст. 1–16):   основы конституционного строя
     * - Глава 2 (ст. 17–64):  права и свободы человека и гражданина
     * - Глава 3 (ст. 65–79):  федеративное устройство
     * - Глава 4 (ст. 80–93):  Президент Российской Федерации
     * - Глава 5 (ст. 94–109): Федеральное Собрание
     * - Глава 6 (ст. 110–117): Правительство Российской Федерации
     * - Глава 7 (ст. 118–129): судебная власть и прокуратура
     * - Глава 8 (ст. 130–133): местное самоуправление
     * - Глава 9 (ст. 134–137): конституционные поправки и пересмотр Конституции
     *
     * @param articleNumber Номер статьи Конституции (от 1 до 137).
     * @return URL страницы с текстом соответствующей главы.
     */
    private fun determineChapterUrl(articleNumber: Int): String {
        return when (articleNumber) {
            in 1..16 -> "http://www.constitution.ru/10003000/10003000-3.htm"
            in 17..64 -> "http://www.constitution.ru/10003000/10003000-4.htm"
            in 65..79 -> "http://www.constitution.ru/10003000/10003000-5.htm"
            in 80..93 -> "http://www.constitution.ru/10003000/10003000-6.htm"
            in 94..109 -> "http://www.constitution.ru/10003000/10003000-7.htm"
            in 110..117 -> "http://www.constitution.ru/10003000/10003000-8.htm"
            in 118..129 -> "http://www.constitution.ru/10003000/10003000-9.htm"
            in 130..133 -> "http://www.constitution.ru/10003000/10003000-10.htm"
            in 134..137 -> "http://www.constitution.ru/10003000/10003000-11.htm"
            else -> "http://www.constitution.ru/10003000/10003000-3.htm"
        }
    }
}