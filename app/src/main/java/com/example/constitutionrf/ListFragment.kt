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


class ListFragment : Fragment() {
    private lateinit var stateViewModel: StateViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

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
                override fun onItemClick(position: Int) {
                    val action = ListFragmentDirections.actionListFragmentToStateFragment(position + 1)
                    findNavController().navigate(action)
                }
            }, stateViewModel)
    }
        }
    }
