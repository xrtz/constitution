package com.example.constitutionrf.sampledata

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.constitutionrf.R
import kotlinx.coroutines.launch

/**
 * Адаптер RecyclerView для отображения списка статей Конституции.
 *
 * Отображает каждую статью в виде карточки с номером и значком избранного.
 * Состояние избранного берётся напрямую из поля [StateModel.favourites] — данные
 * уже загружены в список при создании адаптера.
 * Для обработки нажатий использует [OnItemClickListener].
 *
 * @param articles Список объектов [StateModel] для отображения.
 * @param listener Обработчик нажатий на элементы списка.
 * @param stateViewModel ViewModel для доступа к данным статей.
 */
class RecylerViewItem(
    private val articles: List<StateModel>,
    private val listener: OnItemClickListener,
    private val stateViewModel: StateViewModel
): RecyclerView.Adapter<RecylerViewItem.MyViewHolder>() {

    /**
     * ViewHolder для хранения ссылок на View одного элемента списка.
     *
     * @property text TextView для отображения номера статьи.
     * @property iv_fav ImageView для отображения значка избранного.
     */
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val text: TextView = itemView.findViewById(R.id.tv_item)
        val iv_fav: ImageView = itemView.findViewById(R.id.iv_fav)
    }

    /**
     * Создаёт новый [MyViewHolder] путём раздувания макета `item`.
     *
     * @param parent Родительский ViewGroup для создаваемого View.
     * @param viewType Тип View (все элементы одного типа, параметр не используется).
     * @return Новый экземпляр [MyViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return MyViewHolder(itemView)
    }

    /**
     * Привязывает данные к [MyViewHolder] для элемента на указанной позиции.
     *
     * Отображает номер статьи, устанавливает видимость значка избранного
     * в соответствии с [StateModel.favourites] и назначает обработчик нажатия.
     * При нажатии передаёт в [OnItemClickListener.onItemClick] значение `num - 1`
     * (приводится к 0-индексации для навигации).
     *
     * @param holder ViewHolder для записи данных.
     * @param position Позиция элемента в списке (0-индексированная).
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = articles[position]
        holder.text.text = "Статья ${article.num}"

        if (article.favourites) {
            holder.iv_fav.visibility = View.VISIBLE
        } else {
            holder.iv_fav.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(article.num - 1)
        }
    }

    /**
     * Возвращает общее количество статей в списке.
     *
     * @return Размер списка [articles].
     */
    override fun getItemCount(): Int {
        return articles.size
    }
}