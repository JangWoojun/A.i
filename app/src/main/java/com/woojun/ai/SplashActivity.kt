package com.woojun.ai

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.woojun.ai.util.AiResultList
import com.woojun.ai.util.RetrofitAPI
import com.woojun.ai.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = Firebase.auth

        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<AiResultList> = retrofitAPI.getAiResult(BuildConfig.ESNTLID, BuildConfig.AUTHKEY, 10, null, null, null)

        call.enqueue(object : Callback<AiResultList> {
            override fun onResponse(call: Call<AiResultList>, response: Response<AiResultList>) {
                if (response.isSuccessful) {
                    val aiResultList: AiResultList? = response.body()
                    aiResultList?.let {
                        if (auth.currentUser == null) {
                            val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                            val gson = Gson()
                            intent.putExtra("item", gson.toJson(it))
                            writeStringListToInternalStorage(baseContext, "search_text", arrayListOf())
                            startActivity(intent)
                            finishAffinity()
                        } else {
                            val intent = Intent(this@SplashActivity, MainActivity::class.java)
                            val gson = Gson()
                            intent.putExtra("item", gson.toJson(it))
                            startActivity(intent)
                            finishAffinity()
                        }
                    }
                } else {
                    Log.d("확인1", "에러")
                }
            }

            override fun onFailure(call: Call<AiResultList>, t: Throwable) {
                Log.e("확인2", "API call failed: " + t.message);
            }
        })
    }

    fun writeStringListToInternalStorage(context: Context, filename: String, stringList: ArrayList<String>) {
        try {
            val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            val fileContent = stringList.joinToString("\n")
            outputStream.write(fileContent.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}