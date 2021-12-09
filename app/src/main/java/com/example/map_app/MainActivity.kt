package com.example.map_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPostCreate(savedInstanceState: Bundle?){
        super.onPostCreate(savedInstanceState)
        val navHostFragment : NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
    }
}

fun View.hideKeyboard(){
    val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken,0)
}