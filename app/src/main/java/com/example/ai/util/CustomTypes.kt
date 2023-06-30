package com.example.ai.util

data class ChildInfo(
    val name: String,
    val image: String?,
    val age: String,
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
)

enum class ChildInfoType {
    NEW, DEFAULT
}