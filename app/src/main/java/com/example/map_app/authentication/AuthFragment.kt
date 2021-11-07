package com.example.map_app.authentication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.map_app.R
import com.example.map_app.databinding.FragmentAuthBinding
import com.example.map_app.hideKeyboard
import com.example.map_app.util.getLogDataset
import com.example.map_app.util.getRegDataset

class AuthFragment : Fragment() {

    private lateinit var binding : FragmentAuthBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        recyclerView = binding.recyclerView
        recyclerView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {v.hideKeyboard()}
            }
            v?.onTouchEvent(event) ?: true
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        onCheckLog(authViewModel)

   }

    private fun onCheckReg(authViewModel: AuthViewModel){
        recyclerView.adapter = AuthRecViewAdapter(getRegDataset(resources), authViewModel, ::onCheckLog)
    }

    private fun onCheckLog(authViewModel: AuthViewModel) {
        recyclerView.adapter = AuthRecViewAdapter(getLogDataset(resources), authViewModel, ::onCheckReg)
    }
}