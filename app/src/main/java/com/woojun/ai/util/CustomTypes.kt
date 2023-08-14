package com.woojun.ai.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UserInfo(
    val name: String,
    val email: String,
    val phoneNumber: String,
)

data class AiResult(
    val age: Int,
    val ageNow: Int,
    val alldressingDscd: String,
    val etcSpfeatr: String,
    val msspsnIdntfccd: Int,
    val nm: String,
    val occrAdres: String,
    val occrde: String,
    val rnum: Int,
    val sexdstnDscd: String,
    val tknphotoFile: String,
    val tknphotolength: Int,
    val writngTrgetDscd: String
): Serializable

data class AiResultList(
    val list: List<AiResult>,
    val msg: String,
    val result: String,
    val totalCount: Int
)

enum class ChildInfoType {
    NEW, DEFAULT
}