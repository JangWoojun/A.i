package com.woojun.ai

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.woojun.ai.databinding.ActivityMainBinding
import com.woojun.ai.util.AiResultList
import com.woojun.ai.util.RetrofitAPI
import com.woojun.ai.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<AiResultList> = retrofitAPI.getAiResult()

        call.enqueue(object : Callback<AiResultList> {
            override fun onResponse(call: Call<AiResultList>, response: Response<AiResultList>) {
                if (response.isSuccessful) {
                    val aiResultList: AiResultList? = response.body()
                    aiResultList?.let {
                        Log.d("확인", it.toString())
                    }
                } else {
                    Log.d("확인1", "에러")
                }
            }

            override fun onFailure(call: Call<AiResultList>, t: Throwable) {
                Log.e("확인2", "API call failed: " + t.message);
            }
        })

        window.statusBarColor = Color.TRANSPARENT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.apply {
            bottomNavigation.setItemSelected(R.id.home)

            bottomNavigation.setOnItemSelectedListener {
                when (it) {
                    R.id.home -> navController.navigate(R.id.home)
                    R.id.childrenInfo -> navController.navigate(R.id.childrenInfo)
                    R.id.childrenList -> navController.navigate(R.id.childrenList)
                    R.id.myInfo -> navController.navigate(R.id.myInfo)
                }
            }
        }

    }
}