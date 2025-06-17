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

class MainActivity : AppCompatActivity() {
    private lateinit var stateViewModel: StateViewModel
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