package com.woojun.ai

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.woojun.ai.databinding.ActivityMainBinding
import com.woojun.ai.util.ViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressedTime: Long = 0
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.loadApiData()

        window.statusBarColor = Color.TRANSPARENT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.apply {
            bottomNavigation.setItemSelected(R.id.home)
            bottomNavigation.setOnItemSelectedListener {
                moveBottomNavigation(it)
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavigation.setItemSelected(destination.id)
            }
        }


    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment)
        val currentDestinationId = navController.currentDestination?.id
        val previousDestinationId = navController.previousBackStackEntry?.destination?.id

        if (currentDestinationId != R.id.home) {
            if (previousDestinationId == R.id.childrenList) {
                navController.popBackStack(R.id.childrenList, false)
            } else {
                navController.popBackStack(R.id.home, false)
            }
        } else {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
        hideBottomNavigation(false)
    }

    fun moveBottomNavigation(it: Int) {
        binding.apply {
            val navController = findNavController(R.id.nav_host_fragment)

            when (it) {
                R.id.home -> navController.navigate(R.id.home)
                R.id.childrenList -> navController.navigate(R.id.childrenList)
                R.id.childrenInfo -> navController.navigate(R.id.childrenInfo)
                R.id.setting -> navController.navigate(R.id.setting)
            }
        }
    }

    fun hideBottomNavigation(state: Boolean){
        binding.apply {
            if (state) {
                bottomNavigation.visibility = View.GONE
            } else {
                bottomNavigation.visibility = View.VISIBLE
            }
        }
    }
}