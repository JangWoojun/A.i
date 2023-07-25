package com.woojun.ai.util

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("find/")
    fun getAiResult(): Call<AiResultList>
}