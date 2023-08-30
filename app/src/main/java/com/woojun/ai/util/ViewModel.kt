package com.woojun.ai.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.woojun.ai.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel : ViewModel() {
    private val apiData: MutableLiveData<AiResultList> = MutableLiveData()

    fun loadApiData() {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<AiResultList> = retrofitAPI.getAiResult(BuildConfig.ESNTLID, BuildConfig.AUTHKEY, 50, null, null, null)

        call.enqueue(object : Callback<AiResultList> {
            override fun onResponse(call: Call<AiResultList>, response: Response<AiResultList>) {
                if (response.isSuccessful) {
                    apiData.value = response.body()
                } else {
                    Log.d("확인1", "에러")
                }
            }

            override fun onFailure(call: Call<AiResultList>, t: Throwable) {
                Log.e("확인2", "API call failed: " + t.message);
            }
        })
    }

    fun getApiData(): LiveData<AiResultList> {
        return apiData
    }
}
