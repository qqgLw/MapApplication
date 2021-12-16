package com.example.map_app.authentication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.map_app.R
import com.example.map_app.databinding.FragmentAuthBinding
import com.example.map_app.hideKeyboard
import com.example.map_app.models.UserModel
import com.example.map_app.services.AuthSharedPreferenceService
import com.example.map_app.util.getLogDataset
import com.example.map_app.util.getRegDataset
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var binding : FragmentAuthBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var navBar : BottomNavigationView
    private val authViewModel: AuthViewModel by viewModels()
    @Inject lateinit var authSharedPreferencesService : AuthSharedPreferenceService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        navBar = requireActivity().findViewById((R.id.bottomNavigationView))
        navBar.visibility=View.GONE
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        view?.hideKeyboard()
        navBar.visibility=View.VISIBLE
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {v.hideKeyboard()}
            }
            v?.onTouchEvent(event) ?: true
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        onCheckLog(authViewModel)

        authViewModel.loginData.observe(viewLifecycleOwner) {
            onFormCompleted(it)
        }

        authViewModel.regData.observe(viewLifecycleOwner) {
            onFormCompleted(it)
        }
    }

    private fun onCheckReg(authViewModel: AuthViewModel){
        recyclerView.adapter = AuthRecViewAdapter(getRegDataset(resources), authViewModel, ::onCheckLog)
    }

    private fun onCheckLog(authViewModel: AuthViewModel) {
        recyclerView.adapter = AuthRecViewAdapter(getLogDataset(resources), authViewModel, ::onCheckReg)
    }

    private fun onFormCompleted(result : UserModel?){
        when(result!=null){
            true -> {
                authSharedPreferencesService.saveCurrentUserData(result)
                this.findNavController().navigate(R.id.action_on_auth_completed)
            }
            false ->{
                Snackbar.make(this.requireView(), "Ошибка входа: неверный логин или пароль", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}