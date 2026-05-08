package com.example.mvvm.model

import com.google.gson.annotations.SerializedName

data class ExerciseTranslation(
    val name: String? = "",
    val description: String? = "",
    val language: Int? = 0
)

data class ExerciseCategory(
    val id: Int? = 0,
    val name: String? = "Sin Categoría"
)

data class Muscle(
    val id: Int? = 0,
    val name: String? = ""
)

data class Equipment(
    val id: Int? = 0,
    val name: String? = ""
)

data class Exercise(
    val id: Int,
    val category: ExerciseCategory = ExerciseCategory(),
    val muscles: List<Muscle> = emptyList(),
    @SerializedName("muscles_secondary") val musclesSecondary: List<Muscle> = emptyList(),
    val equipment: List<Equipment> = emptyList(),
    val translations: List<ExerciseTranslation> = emptyList()
)

data class ExerciseResponse(
    val count: Int,
    val results: List<Exercise>
)
