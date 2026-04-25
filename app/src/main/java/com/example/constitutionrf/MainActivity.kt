package com.example.constitutionrf

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.example.constitutionrf.sampledata.AppDatabaseModel
import com.example.constitutionrf.sampledata.StateModel
import com.example.constitutionrf.sampledata.StateViewModel

/**
 * Главная активность приложения «Конституция РФ».
 *
 * Выполняет начальную инициализацию при запуске приложения:
 * - Включает отображение в режиме edge-to-edge.
 * - Настраивает отступы под системные бары (статусбар, навигация).
 * - Инициализирует [StateViewModel] и предзаполняет локальную базу данных
 *   заглушками для всех 137 статей (текст «Статья не найдена»).
 * - Настраивает граф навигации через [NavHostFragment].
 *
 * Реальные тексты статей загружаются лениво в [StateFragment]
 * при первом открытии каждой статьи.
 */
class MainActivity : AppCompatActivity() {

    /**
     * ViewModel для управления данными статей.
     * Разделяется между фрагментами через область видимости активности.
     */
    private lateinit var stateViewModel: StateViewModel

    /**
     * Вызывается при создании активности.
     * Настраивает UI и предзаполняет БД заглушками всех 137 статей.
     *
     * Повторные вызовы (например, при пересоздании активности) не приводят к
     * дублированию записей, так как DAO использует стратегию INSERT OR IGNORE.
     *
     * @param savedInstanceState Сохранённое состояние активности, если имеется.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_fragment_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val provider = ViewModelProvider(this)
        stateViewModel = provider[StateViewModel::class.java]
        for (i in 1..137){
            stateViewModel.addProject(StateModel(0, i, "Статья не найдена", false))
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }
}