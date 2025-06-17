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

class StateFragment : Fragment() {
    private lateinit var stateViewModel: StateViewModel
    private val args: StateFragmentArgs by navArgs()
    private val number: Int get() = args.NUMBER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_state, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvTitle: TextView = view.findViewById(R.id.tv_countOfState)
        val tvContent: TextView = view.findViewById(R.id.textView4)
        tvTitle.text = "Статья $number"
        stateViewModel = ViewModelProvider(requireActivity())[StateViewModel::class.java]
        val imb_fab: ImageButton = view.findViewById(R.id.imb_fav)
        val imb_fav_off: ImageButton = view.findViewById(R.id.imb_fav_off)
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
            ///////////////////////////////
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
//                    stateViewModel.addProject(StateModel(0, number, finalText, false))
                }
            }else {
                tvContent.text = model?.text ?: "Ошибка загрузки"
            }
        }
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
        fun newInstance(number: Int): StateFragment {
            val fragment = StateFragment()
            val args = Bundle()
            args.putInt("NUMBER", number)
            fragment.arguments = args
            return fragment
        }
    }

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
