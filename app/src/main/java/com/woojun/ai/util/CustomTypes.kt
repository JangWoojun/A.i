package com.woojun.ai.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UserInfo(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val photo: String,
    val check: Boolean,
    val children: ArrayList<ChildInfo>
)

data class ChildInfo(
    val id: String,
    val name: String,
    val birthDate: String,
    val sex: String,
    val characteristics: String,
    val photo: String
)
@Parcelize
data class AiResult(
    val age: Int?,
    val ageNow: Int?,
    val alldressingDscd: String?,
    val etcSpfeatr: String?,
    val msspsnIdntfccd: Int?,
    val nm: String?,
    val occrAdres: String?,
    val occrde: String?,
    val rnum: Int?,
    val sexdstnDscd: String?,
    val tknphotoFile: String?,
    val tknphotolength: Int?,
    val writngTrgetDscd: String?
): Parcelable

data class AiResultList(
    val list: List<AiResult>,
    val msg: String,
    val result: String,
    val totalCount: Int
)

enum class ChildInfoType {
    NEW, DEFAULT, SEARCH
}