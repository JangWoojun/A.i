package com.woojun.ai

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.woojun.ai.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.statusBarColor = Color.TRANSPARENT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val navController = findNavController(R.id.nav_host_fragment)

        val bundle = Bundle()
        val item = intent.getStringExtra("item")
        bundle.putString("item", item)
        navController.navigate(R.id.home, bundle)


        binding.apply {
            bottomNavigation.setItemSelected(R.id.home)
            bottomNavigation.setOnItemSelectedListener {
                when (it) {
                    R.id.home -> navController.navigate(R.id.home, bundle)
                    R.id.childrenInfo -> navController.navigate(R.id.childrenInfo)
                    R.id.childrenList -> navController.navigate(R.id.childrenList, bundle)
                    R.id.myInfo -> navController.navigate(R.id.myInfo)
                }
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavigation.setItemSelected(destination.id)
            }
        }


    }

    override fun onBackPressed() {

        val navController = findNavController(R.id.nav_host_fragment)

        if (navController.currentDestination?.id != R.id.home) {
            navController.popBackStack(R.id.home, false)
        } else {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }
}