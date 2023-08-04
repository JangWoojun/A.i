package com.woojun.ai.util

import com.woojun.ai.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {
    @GET(BuildConfig.GETURL)
    fun getAiResult(
        @Query("esntlId") esntlId: String,
        @Query("authKey") authKey: String,
        @Query("rowSize") rowSize: Int,
        @Query("nm") nm: String?,
        @Query("age1") age1: Int?,
        @Query("age2") age2: Int?
    ): Call<AiResultList>

}