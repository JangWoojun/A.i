package com.woojun.ai.util

import java.io.Serializable

data class ChildInfo(
    val name: String,
    val image: String?,
    val age1: String,
    val age2: String,
    val birthday: String,
    val sex: String,
    val date: String,
    val location: String,
    val height: Int,
    val weight: Int,
    val frame: String,
    val faceType: String,
    val hairColor: String,
    val hairType: String,
    val dress: String,
): Serializable

data class UserInfo(
    val name: String,
    val email: String,
    val phoneNumber: String,
)

data class AiResult(
    val age: Int,
    val ageNow: String,
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
)

data class AiResultList(
    val list: List<AiResult>,
    val msg: String,
    val result: String,
    val totalCount: Int
)

enum class ChildInfoType {
    NEW, DEFAULT
}