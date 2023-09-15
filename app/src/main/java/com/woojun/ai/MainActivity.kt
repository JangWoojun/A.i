package com.woojun.ai

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.woojun.ai.databinding.ActivityMainBinding
import com.woojun.ai.util.ProgressUtil
import com.woojun.ai.util.ViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressedTime: Long = 0
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!isNetworkAvailable(this)){
            ProgressUtil.createDialog( // 결과 Dialog 보여주기
                this,
                false,
                "인터넷 연결 실패",
                "인터넷 연결에 실패하였습니다\n" +
                        "A·아이를 이용하시려면 인터넷을 필요합니다\n" +
                        "다시 한번 인터넷 연결이 되었는지 확인해주세요"
            ) {
                if (isNetworkAvailable(this)) {
                    it.dismiss()
                } else {
                    Toast.makeText(this, "인터넷을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

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

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}