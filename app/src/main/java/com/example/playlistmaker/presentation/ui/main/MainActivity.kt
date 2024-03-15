package com.example.playlistmaker.presentation.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_activity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_bottom_panel)
        bottomNavigationView.setupWithNavController(navController)

    }

    fun showNawBar() {
        bottomNavigationView.isVisible = true
    }

    fun hideNavBar() {
        bottomNavigationView.isVisible = false
    }
}